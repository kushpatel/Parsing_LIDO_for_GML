<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:lido="http://www.lido-schema.org">
	
	<xsl:output method="html" 
            encoding="UTF-8"
            indent="no"/>
	
	<xsl:template match="//lido:subjectSet">
		<lido:subjectSet> 
			<lido:subject lido:type="geographicName">
				<lido:subjectPlace>
					<lido:place>
						<lido:namePlaceSet>
							<xsl:copy-of select="node()"></xsl:copy-of>
						</lido:namePlaceSet>
					</lido:place>
				</lido:subjectPlace>
			</lido:subject>
     </lido:subjectSet>
	</xsl:template>
	
	
</xsl:stylesheet>