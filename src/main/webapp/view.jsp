<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<form action="<portlet:actionURL />" method="post" name="<portlet:namespace />fm">
    <input type="submit" value="submit"/>
</form>