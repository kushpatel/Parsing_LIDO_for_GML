<?xml version="1.0"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:lido="http://www.lido-schema.org">

<xsl:output method="html" 
            encoding="UTF-8"
            indent="yes"/>

<xsl:template match="/record/metadata/lido:lido/lido:descriptiveMetadata/lido:objectRelationWrap/lido:subjectWrap">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="lido:subjectSet/lido:subject/lido:subjectPlace">
  <table>
    <xsl:for-each select="lido:Place">
      <!--<xsl:if test="@type = 'vegetable'">-->
        <tr>
          <td><xsl:value-of select="lido:namePlaceSet/lido:appellationValue"/></td>
          <!--<td><xsl:value-of select="lido:gml/gml:Point/gml:coordinates"/></td>-->
       </tr>
      <!--</xsl:if>-->
    </xsl:for-each>
  </table>
</xsl:template>

</xsl:stylesheet>