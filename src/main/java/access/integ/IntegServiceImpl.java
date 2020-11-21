package access.integ;


import muni.dao.CRUDDao;
import muni.model.Model;
import muni.service.SubsystemService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DO NOT make this class public.
 * Implements integ service.
 */
class IntegServiceImpl implements IntegService {
    //    CRUDDao<Model.Person> persDao;
//    CRUDDao<Model.Xref> xrefDao;
//    CRUDDao<Model.PostalAddress> addressDao;
//    CRUDDao<Model.Case> caseDao;
    IntegDao integDao;


    Map<Subsys, SubsystemService> serviceMap = new HashMap<>();

    public IntegServiceImpl(IntegDao integDao) {
        this.integDao = integDao;
    }

    public IntegServiceImpl(CRUDDao<Model.Person> persDao, CRUDDao<Model.Xref> xrefDao, CRUDDao<Model.PostalAddress> addressDao, CRUDDao<Model.Case> caseDao) {
//        this.persDao = persDao;
//        this.xrefDao = xrefDao;
//        this.addressDao = addressDao;
//        this.caseDao = caseDao;
    }

    @Override
    public void setSubSystemService(Subsys subsys, SubsystemService service) {
        if (serviceMap.containsKey(subsys))
            throw new RuntimeException("Initialization error: Attempt to add same type of service twice servocetypee=" + subsys);
        serviceMap.put(subsys, service);
    }

    public SubsystemService getService(String subsysId) {
        Subsys subsys = Subsys.getValueOf(subsysId);
        if (Objects.isNull(subsys)) throw new RuntimeException("Undefined subsystem subsysid=" + subsysId);
        if (!serviceMap.containsKey(subsys))
            throw new RuntimeException("Subsystem Service not set,  subsysid=" + subsysId);
        return serviceMap.get(subsys);
    }

    @Override
    public Model.Person create(Model.Person in) {
        //First save person name, address, get person_id
        Long personMasterId = integDao.create(in);
//        Optional<Model.Person> opt = persDao.get(id);
//        opt.isPresent();//dummy call, find cleaner solution
//        Model.Person fromdb = opt.get();


        //4. TODO Command-SS to create those accounts - api calls
        //5. TODO Update SS-personId to subbaccounts in INTEG_XREF
        List<Model.Xref> toCreate = in.getXrefAccountsMap().values().stream()
                .filter(xref -> xref.hasXrefPersonId() == false) // subsystem not called.
                .collect(Collectors.toList());
        for (Model.Xref xref : toCreate) {
            try {// Call Amanda  / Hansen
                var service = serviceMap.get(Subsys.getValueOf(xref.getXrefSystemId()));
                var xrefPerson = service.person().create(in);
                var xrefWithId = Model.Xref.newBuilder()
                        .setId("" + personMasterId)
                        .setXrefSystemId(xref.getXrefSystemId())//Was a bug here.
                        .setXrefPersonId(xrefPerson.getId()).build();
                Long id = integDao.createOrUpdate(xrefWithId);
                System.out.println("Xref inserted id=" + id);
            } catch (Exception e) {
                //TODO if call to subsystem fails, just ignore, as a backend thread may handle it.
                System.out.println(e.getMessage());
                throw e;
            }
        }
        Model.Person out = integDao.get("" + personMasterId).get();//Once saved, assumed "guaranteed" return
        return out;
    }

    @Override
    public Optional<Model.Person> get(String id) {
        System.out.println("At integServiceImpl person id=" + id);
        Optional<Model.Person> out = integDao.get((id));//Long.valueOf
        System.out.println("At integServiceImpl pers=" + out);
        return out;
    }

    @Override
    public Model.Person update(Model.Person in) {
        var persUpdated = integDao.update(in);
        return persUpdated;//Once saved, assumed "guaranteed" return
    }

    void recordXref(Model.Xref in) {
        integDao.createOrUpdate(in);
    }

    @Override
    public Model.Person recordIntentXref(Model.Person in, Subsys subsysType) {
        // add to XREF_PERSON table
        // call subsys to add Person.
        //
        Optional<Model.Person> optSubSysPerson = Optional.empty();
        try {
            switch (subsysType) {
                case AMANDA:
                    //subsysPerson = amdService.create(in);//call amanda lib
                    break;
                case HANSEN:
                    //subsysPerson = hanService.create(in);//call hansen lib
                default:
                    throw new UnsupportedOperationException("Subsystm id not expected");
            }
        } catch (UnsupportedOperationException e) {
            throw e; // only wrong subsysId need to be
        } catch (Exception e) {
            //Error on subsystem-call [e.g Hansen down] need to be logged (not thrown). And can be rectified scheduled scripts
            //log.error()
            e.printStackTrace();
        }
        // add to XREF_PERSON table
        if (optSubSysPerson.isPresent()) {
            var ssPersId = optSubSysPerson.get().getId();

        } else {

        }
        return null;
    }


    @Override
    public Model.Case create(Model.Case in) {
        //Person  - create: save Person name/address
        return null;
    }

    @Override
    public Model.Case update(Model.Case in) {
        //Person  - create: save Person name/address
        return null;
    }

    @Override
    public Model.Case recordIntentXref(Model.Case in, Subsys subsysType) {
        // If a case must be resolved in subsystems
        return null;
    }

}
