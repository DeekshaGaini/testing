xquery version "1.0";
<query6>
{
(for $emp in doc("company.xml")/Company/Employees/Employee
let $y := doc("company.xml")/Company/Divisions/Division[managerEmpId=$emp/empId]
order by count($y) descending
return <Employee name="{$emp/empName}" phone="{$emp/empPhone}" office="{$emp/empOffice}" count="{count($y)}"/>)[1]
}
</query6>