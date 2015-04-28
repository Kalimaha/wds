<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

	<head>
	    <jsp:include page="include/httpbase.jsp"/>
	    <title>FENIX Web Data Server</title>
	    <link href="axis2-web/css/axis-style.css" rel="stylesheet" type="text/css"/>
	    <link rel='icon' type='image/png' href='http://ldvapp07.fao.org:8030/downloads/fao.png'>
  	</head>

  	<body style="background-color: #A81C7D;">
  	
  	<div style="height: 50px;" ></div>
  	
  	<table align="center" style="width: 800px; align: center; border: 3px solid #1D4589; background-color: #FFFFFF;">
  		
  		<tr>
  			<td align="left" width="80%" style="font-family: sans-serif; font-weight: bold; color: #A81C7D; font-size: 35pt;">FENIX Web Data Server</td>
  			<td width="200px" align="right"><img src="axis2-web/FAOLogo.png" width="50" height="50" alt="FAO"/></td>
  			
  			<td width="200px" align="right"><img src="axis2-web/Axis2Logo.jpg" width="87" height="50" alt="Apache Axis2"/></td>
  		</tr>
  		
  		<tr>
  			<td colspan="3" style="font-family: sans-serif; text-align: justify;">
  				Welcome! If you're reading these words your installation of fwds-web was successful and you can now check 
  				the <a href="services/listServices">list of available web-services</a>. 
  				FENIX Web Data Server uses Axis2 technology to provide data from multiple data sources to FENIX based web applications 
  				and to all the systems with the capability to query web services.
  			</td>
  		</tr>
  		
  		<tr>
  			<td colspan="3" align="center">
  				<a target="_blank" href="http://www.foodsec.org/workstation/">FENIX Portal</a> |
  				<a target="_blank" href="http://axis.apache.org/axis2/java/core/">Apache Axis2</a>  
  			</td>
  		</tr>
  		
  	</table>
    
  	</body>
  	
</html>