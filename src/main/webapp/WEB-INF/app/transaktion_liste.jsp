<%@page import="ejb.KategorieBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

 
<template:base>
    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="suche_text" value="${param.suche_text}" placeholder="Beschreibung"/>
            
            <select name="search_kategorie">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${kategorien}" var="kategorie">
                    <option value="${kategorie.bezeichnung}" ${param.suche_kategorie == kategorie.bezeichnung ? 'selected' : ''}>
                        <c:out value="${kategorie.bezeichnung}" />
                    </option>
                </c:forEach>
            </select>
                
            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Transaktionen --%>
        <c:choose>
            <c:when test="${empty transaktionen}">
                <p>
                    Es wurden keine Transaktionen gefunden.
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Datum</th>
                            <th>Bezeichnung</th>
                            <th>Art</th>
                            <th>Betrag</th>
                            <th>Kategorie</th>
                        </tr>
                    </thead>
                    <c:forEach items="${transaktionen}" var="transaktion">
                        <tr>
                            <td>
                                <fmt:formatDate pattern = "HH:mm dd.MM.yyyy" value = "${transaktion.erstellungsDatum}"/>
                            </td>
                            <td>
                                <c:out value="${transaktion.bezeichnung}"/>
                            </td>
                            <td>
                                <c:out value="${transaktion.art}"/>
                            </td> 
                            <td>
                                <fmt:formatNumber value="${transaktion.betrag}" type="currency" currencySymbol=""/>
                                <c:out value="€"/>
                            </td>   
                            <td>
                                <c:out value="${transaktion.kategorie.bezeichnung}"/>
                            </td>                                                     
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>

