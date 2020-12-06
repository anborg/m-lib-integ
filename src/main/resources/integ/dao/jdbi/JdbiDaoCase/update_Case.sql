update INTEG_CASE set
    status = :status
    , type_id = :typeId
    , address_id = :address.id
    , reportedby_person_id = :reportedByCustomer.id
    , createdby_emp_id = :createdByEmployee
    , description = :description
where
    id= :id