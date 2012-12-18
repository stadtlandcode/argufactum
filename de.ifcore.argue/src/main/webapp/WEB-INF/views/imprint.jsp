<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><spring:message code="imprint.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<div class="page-header">
			<h1><spring:message code="imprint.title" /></h1>
		</div>
		
		<div class="pull-right">
			<div class="well">
				<h2><spring:message code="imprint.libraries.title" /></h2>
				<br />
				<p><spring:message code="imprint.libraries" /></p>
				<ul>
					<li><a href="http://www.springsource.org/spring-framework" target="_blank">Spring Framework</a>, <a href="http://www.springsource.org/spring-security" target="_blank">Spring Security</a> &amp; <a href="http://www.springsource.org/spring-social" target="_blank">Spring Social</a></li>
					<li><a href="http://www.hibernate.org/" target="_blank">Hibernate ORM</a> &amp; <a href="http://ehcache.org/" target="_blank">Ehcache</a></li>
					<li><a href="http://www.eclipse.org/jetty/" target="_blank">Jetty</a> &amp; <a href="http://maven.apache.org/" target="_blank">Apache Maven</a></li>
					<li><a href="https://github.com/Atmosphere/atmosphere" target="_blank">Atmosphere</a></li>
					<li><a href="http://twitter.github.com/bootstrap/" target="_blank">Bootstrap</a></li>
					<li><a href="http://glyphicons.com/" target="_blank">Glyphicons</a></li>
					<li><a href="http://jquery.com/" target="_blank">jQuery</a> + <a href="http://timeago.yarp.com/" target="_blank">timeago</a>, <a href="https://github.com/jquery/jquery-tmpl" target="_blank">tmpl</a> &amp; <a href="http://jqueryui.com/" target="_blank">jQuery UI</a></li>
					<li><a href="http://seleniumhq.org/" target="_blank">Selenium Webdriver</a></li>
				</ul>
			</div>
		</div>
		
		<p>
			<a href="http://www.example.com">Horst Günther</a><br />
			Musterstr. 1<br />
			99999 Musterau<br />
		</p>
		<p>
			T: 0000<br />
			E-Mail: <a class="email">argufactum at argufactum.de</a><br />
		</p>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('a.email').convertEmail();
		});
	</script>
</div>
</body>
</html>