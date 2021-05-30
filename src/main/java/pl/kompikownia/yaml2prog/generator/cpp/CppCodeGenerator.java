package pl.kompikownia.yaml2prog.generator.cpp;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.generator.CodeGenerator;

import java.util.List;

public class CppCodeGenerator implements CodeGenerator {

    @Setter
    @Getter
    private String mainPackageName;

    @Override
    public String generateFile(ClassDefinition classDefinition) {
        val stringBuilder = new StringBuilder();
        generateHeaders(stringBuilder, classDefinition.getParentClass(), classDefinition.getFields());
        generateNamespace(stringBuilder);
        generateClassDefinition(stringBuilder, classDefinition);
        generateClassMembers(stringBuilder, classDefinition);
        appendIndent(4, stringBuilder);
        stringBuilder.append("};\r\n");
        stringBuilder.append("}\r\n");
        return stringBuilder.toString();
    }

    @Override
    public String getFileExtension() {
        return ".h";
    }

    private void generateHeaders(StringBuilder str, ClassDefinition parentClass, List<FieldDefinition> fieldDefinitionList) {
        fieldDefinitionList.stream()
                .filter(fieldDefinition -> fieldDefinition.getType().equals(FieldType.OBJECT))
                .map(fieldDefinition -> fieldDefinition.getRefClass().getName())
                .distinct()
                .forEach(refName -> {
                    str.append("#include \"").append(refName).append(".h\"\r\n");
                });
        if(fieldDefinitionList.stream()
            .anyMatch(FieldDefinition::isArray)) {
            str.append("#include <vector>\r\n");
        }
        if(fieldDefinitionList.stream()
            .anyMatch(field -> field.getType().equals(FieldType.STRING))) {
            str.append("#include <string>\r\n");
        }
        if(parentClass != null) {
            str.append("#include \"").append(parentClass.getName()).append("\\r\n");
        }
    }

    private void generateNamespace(StringBuilder str) {
        str.append("namespace ").append(mainPackageName.replaceAll("\\.", "::")).append(" { \r\n");
    }

    private void generateClassDefinition(StringBuilder str, ClassDefinition classDefinition) {
        appendIndent(4, str);
        str.append("class ").append(classDefinition.getName());
        if(classDefinition.getParentClass() != null) {
            str.append(" : public ").append(classDefinition.getParentClass().getName());
        }
        str.append(" {\r\n");
    }

    private void generateClassMembers(StringBuilder str, ClassDefinition classDefinition) {
        appendIndent(8, str);
        str.append("public: \r\n");
        classDefinition.getFields().forEach(field -> {
            appendIndent(12, str);
            str.append(resolveType(field)).append(" ").append(field.getFieldName()).append(";\r\n");
        });
    }

    private String resolveType(FieldDefinition fieldDefinition) {
        if(fieldDefinition.isArray()) {
            return "vector<"+resolveBasicType(fieldDefinition)+"> ";
        }
        else {
            return resolveBasicType(fieldDefinition);
        }
    }


    private String resolveBasicType(FieldDefinition fieldDefinition) {
        switch(fieldDefinition.getType()) {
            case BOOLEAN:
                return "bool";
            case INTEGER:
                return "int";
            case NUMBER:
                return "double";
            case STRING:
                return "string";
            case OBJECT:
                return fieldDefinition.getRefClass().getName();
            default:
                throw new IllegalArgumentException("Cannot recognize field type " + fieldDefinition.getType());
        }
    }

    private void appendIndent(int indentSize, StringBuilder str) {
        str.append(" ".repeat(Math.max(0, indentSize)));
    }
}
