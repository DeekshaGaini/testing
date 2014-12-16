xquery version "1.0";
<query3>
{
let $empsmith := doc("company.xml")/Company/Employees/Employee[empName="PSmith"]
let $empwong := doc("company.xml")/Company/Employees/Employee[empName="Wong"]
let $deptwong := distinct-values(doc("company.xml")/Company/Divisions/Division[managerEmpId=$empwong/empId]/housedDeptId)
let $deptsmith := distinct-values(doc("company.xml")/Company/Divisions/Division[managerEmpId=$empsmith/empId and housedDeptId !=$deptwong]/housedDeptId)
for $x in $deptsmith,
$y in doc("company.xml")/Company/Departments/Department
where $y/deptId=$x
return data($y/deptName)
}
</query3>