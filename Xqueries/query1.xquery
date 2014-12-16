xquery version "1.0";
<query1>
{
for $dept in doc("company.xml")/Company/Departments/Department,
$emp in doc("company.xml")/Company/Employees/Employee,
$work in doc("company.xml")/Company/Worksfor/Works,
$div in doc("company.xml")/Company/Divisions/Division
where
($dept/deptId=$div/housedDeptId) and ($emp/empName="PSmith"  or $emp/empName="Jack") and ($emp/empId=$work/empId) and ($div/divId=$work/divId) and ($work/percentTime>=50)
return data($dept/deptName)
}
</query1>