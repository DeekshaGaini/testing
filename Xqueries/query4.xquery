xquery version "1.0";
<query4>
{
for $emp in doc("company.xml")/Company/Employees/Employee
let $y := doc("company.xml")/Company/Divisions/Division[managerEmpId=$emp/empId]
where count($y)=0
order by $emp/empName descending
return <Employee name="{$emp/empName}" phone="{$emp/empPhone}" office="{$emp/empOffice}"/>
}
</query4>