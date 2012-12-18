<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="modificationLog" required="true" type="java.lang.Object" description="instance of class LogModel" %>
<span class="editor-note">
	(<spring:message code="argument.editor" arguments="${modificationLog.authorName}" htmlEscape="true" />
	<abbr class="timeago" title="${modificationLog.dateTimeGmt}">${modificationLog.dateTimeLocalized}</abbr>)
</span>