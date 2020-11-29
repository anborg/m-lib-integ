package access.integ;


import muni.model.Model;
import muni.service.SubsystemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DO NOT make this class public.
 * Implements integ service.
 */
class IntegServiceImpl implements IntegService {

    IntegDao integDao;

    Map<Subsys, SubsystemService> serviceMap = new HashMap<>();

    public IntegServiceImpl(IntegDao integDao) {
        this.integDao = integDao;
    }

    @Override
    public void setSubsystemService(Subsys subsys, SubsystemService service) {
        if (serviceMap.containsKey(subsys))
            throw new RuntimeException("Initialization error: Attempt to add same type of service twice serviceType=" + subsys);
        serviceMap.put(subsys, service);
    }
    @Override
    public SubsystemService getSubsystemService(String subsysId) {
        Subsys subsys = Subsys.getValueOf(subsysId);
        if (subsys.equals(Subsys.UNDEFINED)) throw new RuntimeException("Undefined subsystem subsysid=" + subsysId);
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
                .filter(xref -> xref.hasXrefId() == false) // subsystem not called.
                .collect(Collectors.toList());
        for (Model.Xref xref : toCreate) {
            try {// Call Amanda  / Hansen
                Subsys subsys = Subsys.getValueOf(xref.getXrefSystemId());
                var service = getSubsystemService(xref.getXrefSystemId());
                var xrefPerson = service.person().create(in);
                var xrefWithId = Model.Xref.newBuilder()
                        .setId("" + personMasterId)
                        .setXrefSystemId(xref.getXrefSystemId())//Was a bug here.
                        .setXrefId(xrefPerson.getId()).build();
                Long id = integDao.create(xrefWithId);
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
    public Optional<Model.Person> getPerson(String id) {
        System.out.println("At integServiceImpl person id=" + id);
        Optional<Model.Person> out = integDao.get((id));//Long.valueOf
        System.out.println("At integServiceImpl pers=" + out);
        return out;
    }

    @Override
    public Optional<Model.Person> getSubsystemPerson(Model.Xref xref) {
        var service = getSubsystemService(xref.getXrefSystemId());
        var xrefPerson = service.person().get(xref.getXrefId());
        return xrefPerson;
    }

    @Override
    public List<Model.Person> personsRecent() {
        return integDao.getRecentPersons();
    }

    private Model.Person updateSubsystemPerson(Subsys subsys, Model.Person in) {
        var subService = getSubsystemService(subsys.toString());
        var xrefPerson = subService.person().update(in);
        return xrefPerson;
    }

    @Override
    public Model.Person update(Model.Person in) {
        var persUpdated = integDao.update(in);

        for(var xref: in.getXrefAccountsMap().values()){
            if (xref.hasId() && xref.hasXrefSystemId() && xref.hasXrefId()) {
                var subsystemPerson = IntegUtil.buildSubsystemPerson(xref, in);
                updateSubsystemPerson(Subsys.getValueOf(xref.getXrefSystemId()), subsystemPerson);
            }
        }

        return persUpdated;//Once saved, assumed "guaranteed" return
    }



    void recordXref(Model.Xref in) {
        integDao.create(in);
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
