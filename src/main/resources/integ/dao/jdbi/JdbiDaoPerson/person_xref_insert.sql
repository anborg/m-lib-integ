-- One person, present in n sub-systems of an organization
insert into
    INTEG_XREF_PERSON
    (id, xref_sys_id, xref_id)
values
    ( :id , :xrefSystemId, :xrefId )