package access.integ;

import muni.model.Model;

public class DataQuality {

    public static boolean isValidForInsert(Model.PostalAddress in) {
        if (null != in && in.getId().isEmpty() == true) {// ID must be NULL
            //mandatory check
            if (in.getStreetNum().isEmpty() == false
                    && in.getStreetName().isEmpty() == false
                    && in.getPostalCode().isEmpty() == false
                    && in.hasCreateTime() //NO create ts
                    && in.hasUpdateTime() //NO update ts
            ) {// streetname , streetnum, postcal code -
                //if postal code is prsent, just the
                //assume Canada - country code
                // city can be derived from postal code.
                return true; // valid insert
            } else {// new obj - mandarory FAIL
                throw new RuntimeException("Insert=no ID, but the objct must have streetNum#, streetName, postalcode. Hint : Mandatory field missing?");
            }
        }
        return false;
    }

    public static boolean isvalidForUpdate(Model.PostalAddress in) {
        // explict signal to update
        return null != in
                && in.getId().isEmpty() == false // ID must NOT be NULL - preexisting
                && in.hasCreateTime() // signal: already created
                && in.hasUpdateTime() // signal: already created
                && in.getDirty() == true;
    }

    public static class Person {
        public static boolean isValidForInsert(Model.Person in) {
            if (null != in && in.getId().isEmpty() == true) {// ID must be NULL
                // Yes no id, so insert.  Is data sufficient for business?
                if (in.getFirstName().isEmpty() == false //fn NOT empty
                        && in.getLastName().isEmpty() == false //ln NOT empty
                        && in.hasCreateTime() == false//NO create ts
                        && in.hasUpdateTime() == false//NO update ts
                        && (in.hasAddress()  // one of email/ phone/ address
                        || in.getEmail().isEmpty() == false
                        || in.getPhone1().isEmpty() == false
                        || in.getPhone2().isEmpty() == false)//one contact channel
                ) {//valid personfor insert
                    return true;
                } else {//new obj - but mandarory absent
                    throw new RuntimeException("Insert=no ID, but the objct must have fname, lname and one contact. Hint : Mandatory field missing?");
                }
            }//id empty
            return false;
        }//isvalidfor-insert

        public static boolean isvalidForUpdate(Model.Person in) {
            if (null != in && in.getId().isEmpty() == false) {// ID must NOT be NULL - preexisting
                if (in.hasCreateTime() // signal: already created
                        && in.hasUpdateTime() // signal: already created
                        && in.getDirty() == true // explict signal to update
                ) {
                    return true;
                } else {
                    throw new RuntimeException("Update=ID-present. BUT! timestamp expected for PRE-exiting obj, dirty=true expected. Hint : Forgot to set dirty=true ?");

                }
            }
            return false;
        }//isvalidfor-update
    }
}
