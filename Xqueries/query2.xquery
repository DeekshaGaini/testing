xquery version "1.0";
<query2>
{
let $emp := doc("company.xml")/Company/Employees/Employee
let $empcount := count( $emp/empId )
for $x in doc("company.xml")/Company/Divisions/Division
let $y := doc("company.xml")/Company/Worksfor/Works[divId=$x/divId]
where count($y)=$empcount
return data($x/divName)
}
</query2>