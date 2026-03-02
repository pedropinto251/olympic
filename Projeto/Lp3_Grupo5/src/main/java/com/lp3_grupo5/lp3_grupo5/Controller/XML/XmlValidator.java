package com.lp3_grupo5.lp3_grupo5.Controller.XML;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Classe responsável pela validação de documentos XML contra um esquema XSD.
 * <p>
 * Esta classe fornece métodos para validar um arquivo XML em relação a um esquema XSD fornecido.
 * A validação é feita verificando se o XML segue as regras e estrutura definidas no esquema XSD.
 * </p>
 */
public class XmlValidator {

    /**
     * Valida um documento XML utilizando um esquema XSD.
     * <p>
     * Este método recebe os caminhos dos arquivos XML e XSD, carrega o esquema a partir do arquivo XSD
     * e valida o conteúdo do arquivo XML em conformidade com o esquema. Caso o XML esteja em conformidade,
     * o método retorna <code>true</code>. Caso contrário, ele retorna <code>false</code> e imprime a mensagem
     * de erro se houver problemas durante o processo de validação.
     * </p>
     *
     * @param xmlPath O caminho do arquivo XML a ser validado.
     * @param xsdPath O caminho do arquivo XSD que define o esquema.
     * @return <code>true</code> se o XML for válido de acordo com o XSD, <code>false</code> caso contrário.
     */
    public static boolean validateXmlSchema(String xmlPath, String xsdPath) {
        try {
            // Cria uma fábrica de esquemas para o XSD
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // Carrega o esquema a partir do caminho fornecido
            Schema schema = factory.newSchema(new File(xsdPath));
            // Cria um validador a partir do esquema
            Validator validator = schema.newValidator();
            // Valida o arquivo XML contra o esquema
            validator.validate(new StreamSource(new File(xmlPath)));
            return true; // Se a validação for bem-sucedida, retorna true
        } catch (IOException | SAXException e) {
            // Em caso de erro, imprime a mensagem de erro e retorna false
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }
}
