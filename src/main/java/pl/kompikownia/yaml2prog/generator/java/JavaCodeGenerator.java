package pl.kompikownia.yaml2prog.generator.java;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import pl.kompikownia.yaml2prog.definition.ClassDefinition;
import pl.kompikownia.yaml2prog.definition.FieldDefinition;
import pl.kompikownia.yaml2prog.definition.FieldType;
import pl.kompikownia.yaml2prog.exception.FileParseException;
import pl.kompikownia.yaml2prog.generator.CodeGenerator;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class JavaCodeGenerator implements CodeGenerator {

    @Setter
    @Getter
    private String mainPackageName;

    @Override
    public String generateFile(ClassDefinition classDefinition) {
        val stringBuilder = new StringBuilder();
        setPackageName(stringBuilder);
        generateImports(stringBuilder, classDefinition.getParentClass(), classDefinition.getFields());
        generateClassAnnotations(stringBuilder, classDefinition.getParentClass() != null);
        generateClassDefinition(stringBuilder, classDefinition);
        if(classDefinition.getParentClass() != null) {
            generateConstructorsWithBuilderAnnotation(stringBuilder, classDefinition.getName(),
                    Optional.ofNullable(classDefinition.getParentClass())
                            .orElse(ClassDefinition.builder().build()).getFields(),
                    classDefinition.getFields());
        }
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

    private void generateClassAnnotations(StringBuilder str, boolean hasParentClass) {
        str.append("@Getter\r\n");
        str.append("@Setter\r\n");
        if (!hasParentClass) {
            str.append("@Builder\r\n");
        }
    }

    private void generateClassDefinition(StringBuilder str, ClassDefinition classDefinition) {
        str.append("public class ").append(classDefinition.getName());
        if (classDefinition.getParentClass() != null) {
            str.append(" extends ").append(classDefinition.getParentClass().getName());
        }
        str.append(" {\r\n");
    }

    private void generateClassMembers(StringBuilder str, ClassDefinition classDefinition) {
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
        if(fieldDefinition.isArray()) {
            return "List<"+resolveBasicType(fieldDefinition)+"> ";
        }
        else {
            return resolveBasicType(fieldDefinition);
        }
    }

    private String resolveBasicType(FieldDefinition fieldDefinition) {
        switch(fieldDefinition.getType()) {
            case BOOLEAN:
                return "Boolean";
            case INTEGER:
                return "Integer";
            case NUMBER:
                return "Double";
            case STRING:
                return "String";
            case OBJECT:
                return fieldDefinition.getRefClass().getName();
            default:
                throw new IllegalArgumentException("Cannot recognize field type " + fieldDefinition.getType());
        }
    }

    private void generateImports(StringBuilder str, ClassDefinition parentClass, List<FieldDefinition> fields) {
        fields.stream()
                .filter(fieldDefinition -> fieldDefinition.getType().equals(FieldType.OBJECT))
                .map(fieldDefinition -> fieldDefinition.getRefClass().getName())
                .distinct()
                .forEach(refName -> {
                    str.append("import ").append(mainPackageName).append(".").append(refName).append(";\r\n");
                });
        if(fields.stream()
                .anyMatch(FieldDefinition::isArray)) {
            str.append("import java.util.List;\r\n");
        }
        if(parentClass != null) {
            str.append("import ").append(mainPackageName).append(".").append(parentClass.getName()).append(";\r\n");
        }
        str.append("import lombok.Setter;\r\n");
        str.append("import lombok.Getter;\r\n");
        str.append("import lombok.Builder;\r\n");
        str.append("\r\n");
    }

    private void generateConstructorsWithBuilderAnnotation(StringBuilder str,
                                                           String className,
                                                           List<FieldDefinition> parentClassFieldDefinitions,
                                                           List<FieldDefinition> currentClassFieldDefinitions) {
        appendIndent(4, str);
        str.append("@Builder\r\n");
        appendIndent(4, str);
        str.append("protected ").append(className).append("(");
        parentClassFieldDefinitions.forEach(fieldDefinition -> {
            str.append(resolveType(fieldDefinition)).append(" ")
                    .append(fieldDefinition.getFieldName());
            str.append(",");
        });
        currentClassFieldDefinitions.forEach(fieldDefinition -> {
            str.append(resolveType(fieldDefinition)).append(" ")
                    .append(fieldDefinition.getFieldName());
            str.append(",");
        });
        str.setLength(str.length() - 1);
        str.append(") {\r\n");
        if (parentClassFieldDefinitions.size() > 0) {
           appendIndent(8, str);
           str.append("super(");
           parentClassFieldDefinitions.forEach(fieldDefinition -> {
               str.append(fieldDefinition.getFieldName()).append(",");
           });
           str.setLength(str.length() - 1);
           str.append(");\r\n");
        }
        currentClassFieldDefinitions.forEach(fieldDefinition -> {
            appendIndent(8, str);
            str.append("this.").append(fieldDefinition.getFieldName())
                    .append(" = ").append(fieldDefinition.getFieldName())
                    .append(";\r\n");
        });
        appendIndent(4, str);
        str.append("}\r\n");
    }
}
