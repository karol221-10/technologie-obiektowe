package pl.kompikownia.yaml2prog.generator.java;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.generator.CodeGenerator;

import java.util.List;

public class JavaCodeGenerator implements CodeGenerator {

    @Setter
    @Getter
    private String mainPackageName;

    @Override
    public String generateFile(ClassDefinition classDefinition) {
        val stringBuilder = new StringBuilder();
        setPackageName(stringBuilder);
        generateImports(stringBuilder, classDefinition.getFields());
        generateClassAnnotations(stringBuilder);
        generateClassMembers(stringBuilder, classDefinition);
        return stringBuilder.toString();
    }

    @Override
    public String getFileExtension() {
        return ".java";
    }

    private void setPackageName(StringBuilder str) {
        str.append("package ").append(mainPackageName).append(";\r\n\r\n");
    }

    private void generateClassAnnotations(StringBuilder str) {
        str.append("@Getter\r\n");
        str.append("@Builder\r\n");
    }

    private void generateClassMembers(StringBuilder str, ClassDefinition classDefinition) {
        str.append("public class ").append(classDefinition.getName()).append(" {\r\n");
        classDefinition.getFields().forEach(field -> {
            appendIndent(4, str);
            str.append("private ").append(resolveType(field)).append(" ").append(field.getFieldName()).append(";\r\n");
        });
        str.append("}\r\n");
    }

    private void appendIndent(int indentSize, StringBuilder str) {
        str.append(" ".repeat(Math.max(0, indentSize)));
    }

    private String resolveType(FieldDefinition fieldDefinition) {
        switch(fieldDefinition.getType()) {
            case NUMBER:
                return "Integer";
            case STRING:
                return "String";
            case OBJECT:
                return fieldDefinition.getRefName();
            default:
                throw new IllegalArgumentException("Cannot recognize field type " + fieldDefinition.getType());
        }
    }

    private void generateImports(StringBuilder str, List<FieldDefinition> fields) {
        fields.stream()
                .filter(fieldDefinition -> fieldDefinition.getType().equals(FieldType.OBJECT))
                .map(FieldDefinition::getRefName)
                .distinct()
                .forEach(refName -> {
                    str.append("import ").append(mainPackageName).append(".").append(refName).append(";\r\n");
                });
        str.append("import lombok.Getter;\r\n");
        str.append("import lombok.Builder;\r\n");
        str.append("\r\n");
    }
}
