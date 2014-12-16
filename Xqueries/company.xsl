<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:template match="/">
<html>
<style type="text/css">
body{
font-size:12px;
font-family:arial;
}
.employee_header{
background-color : yellow;
font-size: 24px;
font-weight: bold;
color: red;
}
.department_header{
font-size:24px;
background-color:LightGreen;
font-weight:bold;
color: red;
}
</style>
	<head>
		<title></title>
	</head>
	<body>
	<div class="employee_header">
	<h2>Employee Information</h2>
	</div>
	<xsl:for-each select="Company/Employees/Employee">
    Employee <b><xsl:value-of select="empName"/></b> works from <b><xsl:value-of select="empOffice"/></b> office. <b><xsl:value-of select="empName"/></b> works for <b><xsl:value-of select="count(//Works/empId[.=current()/empId])"/></b> division(s), which are 
    <xsl:for-each select="//Works/empId[.=current()/empId]">
    <xsl:choose>
    <xsl:when test="position() = last()">
    <xsl:variable name="divisionID" select="../divId"/>
    <xsl:for-each select="//Division">
    <xsl:if test="$divisionID[.=current()/divId]">
     <b>and <xsl:value-of select="current()/divName"/>. </b>
    </xsl:if>    
    </xsl:for-each>
    </xsl:when>
    <xsl:otherwise>
    <xsl:variable name="divisionID" select="../divId"/>
    <xsl:for-each select="//Division">
    <xsl:if test="$divisionID[.=current()/divId]">
    <b><xsl:value-of select="current()/divName"/>, </b>
    </xsl:if>    
    </xsl:for-each>
    </xsl:otherwise>
    </xsl:choose>
    </xsl:for-each>
    <b><xsl:value-of select="empName"/></b> manages <b><xsl:value-of select="count(//Division/managerEmpId[.=current()/empId])"/></b> division(s)
    <xsl:choose>
					<xsl:when test="count(//Division/managerEmpId[.=current()/empId])=0">.</xsl:when>
					<xsl:when test="count(//Division/managerEmpId[.=current()/empId])=1">, which is 
					<xsl:for-each select="//Division/managerEmpId[.=current()/empId]">
					<b><xsl:value-of select="../divName"/>. </b>
					</xsl:for-each>
					</xsl:when>
	<xsl:otherwise>
    , which are 
    <xsl:for-each select="//Division/managerEmpId[.=current()/empId]">
    <xsl:choose>
    <xsl:when test="position() = last()">
     <b>and <xsl:value-of select="../divName"/>. </b>
    </xsl:when>
    <xsl:otherwise>
    <b><xsl:value-of select="../divName"/>, </b>
    </xsl:otherwise>
    </xsl:choose>
    </xsl:for-each>
    </xsl:otherwise>
    </xsl:choose>
    
    <b><xsl:value-of select="empName"/></b> works  for  the  most  time  with  the 
    
    <xsl:for-each select="//Works/empId[. = current()/empId]">
    <xsl:sort select="../percentTime" order="descending" data-type="number"/>
    <xsl:if test="position()= 1">
    <xsl:variable name="max" select="../divId" />
    <xsl:for-each select="//Division">
    <xsl:if test="current()/divId=$max">
	<b><xsl:value-of select="current()/divName"/></b>
	division.
    </xsl:if>
    </xsl:for-each>
    </xsl:if>
    </xsl:for-each>
    <br></br>
    <br></br>
	</xsl:for-each>
	<div class="department_header">
	<h2>Department Information</h2>
	</div>
	<xsl:for-each select="Company/Departments/Department">
	Department <b><xsl:value-of select="current()/deptName"/></b> houses 
	<b><xsl:value-of select="count(//Division/housedDeptId[.=current()/deptId])"/></b> division(s): 
	<xsl:for-each select="//Division/housedDeptId[.=current()/deptId]">
	<xsl:choose>
    <xsl:when test="position() = last()">
    <b>and <xsl:value-of select="../divName"/>. </b>
    </xsl:when>
    <xsl:otherwise>
    <b><xsl:value-of select="../divName"/>, </b>
    </xsl:otherwise>
    </xsl:choose>
	</xsl:for-each>
	<br></br>
	<br></br>
	</xsl:for-each>
	</body>
</html>
</xsl:template>
</xsl:stylesheet>
