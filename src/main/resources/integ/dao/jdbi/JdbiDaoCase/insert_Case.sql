insert into INTEG_CASE
    (status ,type_id ,description,reportedby_person_id, createdby_emp_id,address_id)
values
    ( :status, :typeId, :description, :reportedByCustomer.id, :createdByEmployee, :address.id )
