xquery version "1.0";
<query5>
{
let $emp := doc("company.xml")/Company/Employees/Employee
let $empcount := count( $emp/empId )
let $work := doc("company.xml")/Company/Worksfor/Works
let $workcount := count($work/divId)
let $result := $workcount div $empcount
return $result
}
</query5>
