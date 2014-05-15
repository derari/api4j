package org.cthul.api4j.gen;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.expression.AnnotationValue;
import com.thoughtworks.qdox.model.expression.Expression;
import com.thoughtworks.qdox.writer.ModelWriter;
import com.thoughtworks.qdox.writer.ModelWriterFactory;
import com.thoughtworks.qdox.writer.impl.IndentBuffer;
import java.util.*;

public class FixedModelWriter
    implements ModelWriter
{
    public static final ModelWriterFactory FACTORY = new ModelWriterFactory() {
        @Override
        public ModelWriter newInstance() {
            return new FixedModelWriter();
        }
    };
    
    private IndentBuffer buffer = new IndentBuffer();

    public FixedModelWriter() {
        buffer.setIndentation("    ");
    }
    
    /**
     * All information is written to this buffer.
     * When extending this class you should write to this buffer
     * 
     * @return the buffer
     */
    protected final IndentBuffer getBuffer()
    {
        return buffer;
    }
    
    /** {@inheritDoc} */
    @Override
    public ModelWriter writeSource( JavaSource source )
    {
        // package statement
        writePackage( source.getPackage() );

        // import statement
        for ( String imprt : source.getImports() )
        {
            buffer.write( "import " );
            buffer.write( imprt );
            buffer.write( ';' );
            buffer.newline();
        }
        if ( source.getImports().size() > 0 )
        {
            buffer.newline();
        }

        // classes
        for ( ListIterator<JavaClass> iter = source.getClasses().listIterator(); iter.hasNext(); )
        {
            JavaClass cls = iter.next();
            writeClass( cls );
            if ( iter.hasNext() )
            {
                buffer.newline();
            }
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writePackage( JavaPackage pckg )
    {
        if ( pckg != null )
        {
            commentHeader( pckg );
            buffer.write( "package " );
            buffer.write( pckg.getName() );
            buffer.write( ';' );
            buffer.newline();
            buffer.newline();
        }
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writeClass( JavaClass cls )
    {
        commentHeader( cls );

        writeAccessibilityModifier( cls.getModifiers() );
        writeNonAccessibilityModifiers( cls.getModifiers() );

        buffer.write( cls.isEnum() ? "enum " : cls.isInterface() ? "interface " : cls.isAnnotation() ? "@interface "
                        : "class " );
        buffer.write( cls.getName() );
        typeParameters(cls.getTypeParameters(), false);
        // subclass
        if ( cls.getSuperClass() != null )
        {
            String className = cls.getSuperClass().getFullyQualifiedName();
            if ( !"java.lang.Object".equals( className ) && !"java.lang.Enum".equals( className ) )
            {
                buffer.write( " extends " );
                buffer.write( cls.getSuperClass().getGenericCanonicalName() );
            }
        }

        // implements
        if ( cls.getImplements().size() > 0 )
        {
            buffer.write( cls.isInterface() ? " extends " : " implements " );

            for ( ListIterator<JavaType> iter = cls.getImplements().listIterator(); iter.hasNext(); )
            {
                buffer.write( iter.next().getGenericCanonicalName() );
                if ( iter.hasNext() )
                {
                    buffer.write( ", " );
                }
            }
        }

        return writeClassBody( cls );
    }

    private ModelWriter writeClassBody( JavaClass cls )
    {
        buffer.write( " {" );
        buffer.newline();
        buffer.indent();

        // fields
        for ( JavaField javaField : cls.getFields() )
        {
            buffer.newline();
            writeField( javaField );
        }

        // constructors
        for ( JavaConstructor javaConstructor : cls.getConstructors() )
        {
            buffer.newline();
            writeConstructor( javaConstructor );
        }
        
        // methods
        for ( JavaMethod javaMethod : cls.getMethods() )
        {
            buffer.newline();
            writeMethod( javaMethod );
        }

        // inner-classes
        for ( JavaClass innerCls : cls.getNestedClasses() )
        {
            buffer.newline();
            writeClass( innerCls );
        }

        buffer.deindent();
        buffer.newline();
        buffer.write( '}' );
        buffer.newline();
        return this;
    }
    
    /** {@inheritDoc} */
    @Override
    public ModelWriter writeInitializer( JavaInitializer init )
    {
        if ( init.isStatic() )
        {
            buffer.write( "static " );
        }
        buffer.write( '{' );
        buffer.newline();
        buffer.indent();

        buffer.write( init.getBlockContent() );
        
        buffer.deindent();
        buffer.newline();
        buffer.write( '}' );
        buffer.newline();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writeField( JavaField field )
    {
        commentHeader( field );

        writeAllModifiers( field.getModifiers() );
        if ( !field.isEnumConstant() )
        {
            buffer.write( field.getType().getGenericCanonicalName() );
            buffer.write( ' ' );
        }
        buffer.write( field.getName() );
        
        if ( field.isEnumConstant() )
        {
            if ( field.getEnumConstantArguments() != null && !field.getEnumConstantArguments().isEmpty() )
            {
                buffer.write( "( " );
                for( Iterator<Expression> iter = field.getEnumConstantArguments().listIterator(); iter.hasNext(); )
                {
                    buffer.write( iter.next().getParameterValue().toString() );
                    if( iter.hasNext() )
                    {
                        buffer.write( ", " );
                    }
                }
                buffer.write( " )" );
            }
            if ( field.getEnumConstantClass() != null )
            {
                writeClassBody( field.getEnumConstantClass() );
            }
        }
        else
        {
            if ( field.getInitializationExpression() != null && field.getInitializationExpression().length() > 0 )
            {
                {
                    buffer.write( " = " );
                }
                buffer.write( field.getInitializationExpression() );
            }
        }
        buffer.write( ';' );
        buffer.newline();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writeConstructor( JavaConstructor constructor )
    {
        commentHeader( constructor );
        writeAllModifiers( constructor.getModifiers() );

        buffer.write( constructor.getName() );
        buffer.write( '(' );
        for ( ListIterator<JavaParameter> iter = constructor.getParameters().listIterator(); iter.hasNext(); )
        {
            writeParameter( iter.next() );
            if ( iter.hasNext() )
            {
                buffer.write( ", " );
            }
        }
        buffer.write( ')' );

        if ( constructor.getExceptions().size() > 0 )
        {
            buffer.write( " throws " );
            for ( Iterator<JavaClass> excIter = constructor.getExceptions().iterator(); excIter.hasNext(); )
            {
                buffer.write( excIter.next().getGenericCanonicalName() );
                if ( excIter.hasNext() )
                {
                    buffer.write( ", " );
                }
            }
        }

        sourceCode(constructor.getSourceCode(), " {\n}");

        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writeMethod( JavaMethod method )
    {
        commentHeader( method );
        writeAccessibilityModifier( method.getModifiers() );
        writeNonAccessibilityModifiers( method.getModifiers() );
        typeParameters(method.getTypeParameters(), true);
        buffer.write( method.getReturnType().getGenericCanonicalName() );
        buffer.write( ' ' );
        buffer.write( method.getName() );
        buffer.write( '(' );
        for ( ListIterator<JavaParameter> iter = method.getParameters().listIterator(); iter.hasNext(); )
        {
            writeParameter( iter.next() );
            if ( iter.hasNext() )
            {
                buffer.write( ", " );
            }

        }
        buffer.write( ')' );
        if ( method.getExceptions().size() > 0 )
        {
            buffer.write( " throws " );
            for ( Iterator<JavaClass> excIter = method.getExceptions().iterator(); excIter.hasNext(); )
            {
                buffer.write( excIter.next().getGenericCanonicalName() );
                if ( excIter.hasNext() )
                {
                    buffer.write( ", " );
                }
            }
        }
        sourceCode(method.getSourceCode(), ";");
        return this;
    }

    private void writeNonAccessibilityModifiers( List<String> modifiers )
    {
        for ( String modifier : modifiers )
        {
            if ( !modifier.startsWith( "p" ) )
            {
                buffer.write( modifier );
                buffer.write( ' ' );
            }
        }
    }

    private void writeAccessibilityModifier( List<String> modifiers )
    {
        for ( String modifier : modifiers )
        {
            if ( modifier.startsWith( "p" ) )
            {
                buffer.write( modifier );
                buffer.write( ' ' );
            }
        }
    }

    private void writeAllModifiers( List<String> modifiers )
    {
        for ( String modifier : modifiers )
        {
            buffer.write( modifier );
            buffer.write( ' ' );
        }
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writeAnnotation( JavaAnnotation annotation )
    {
        buffer.write( '@' );
        buffer.write( annotation.getType().getGenericCanonicalName() );
        if ( !annotation.getPropertyMap().isEmpty() )
        {
            buffer.indent();
            buffer.write( '(' );
            Iterator<Map.Entry<String, AnnotationValue>> iterator = annotation.getPropertyMap().entrySet().iterator();
            while ( iterator.hasNext() )
            {
                Map.Entry<String, AnnotationValue> entry = iterator.next();
                buffer.write( entry.getKey() );
                buffer.write( '=' );
                buffer.write( entry.getValue().toString() );
                if ( iterator.hasNext() )
                {
                    buffer.write( ',' );
                    buffer.newline();
                }
            }
            buffer.write( ')' );
            buffer.deindent();
        }
        buffer.newline();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public ModelWriter writeParameter( JavaParameter parameter )
    {
        commentHeader( parameter );
        buffer.write( parameter.getGenericCanonicalName() );
        if ( parameter.isVarArgs() )
        {
            buffer.write( "..." );
        }
        buffer.write( ' ' );
        buffer.write(  parameter.getName() );
        return this;
    }
    
    protected void typeParameters(List<? extends JavaTypeVariable<?>> typeParams, boolean trailingSpace) {
        if (typeParams.isEmpty()) return;
        buffer.write("<");
        boolean first = true;
        for (JavaTypeVariable<?> p: typeParams) {
            if (first) first = false;
            else buffer.write(",");
            buffer.write(p.getGenericValue());
        }
        buffer.write(">");
        if (trailingSpace) buffer.write(" ");
    }
    
    protected void sourceCode(String code, String emptyCode) {
        if ( code != null && code.length() > 0 )
        {
            buffer.write( " {" );
            buffer.indent();
            for (String line: code.split("\n")) {
                buffer.newline();
                buffer.write(line);
            }
            buffer.deindent();
            buffer.newline();
            buffer.write( '}' );
            buffer.newline();
        }
        else
        {
            for (String line: emptyCode.split("\n")) {
                buffer.write(line);
                buffer.newline();
            }
        }
    }

    protected void commentHeader( JavaAnnotatedElement entity )
    {
        if ( entity.getComment() != null || ( entity.getTags().size() > 0 ) )
        {
            buffer.write( "/**" );
            buffer.newline();

            if ( entity.getComment() != null && entity.getComment().length() > 0 )
            {
                
                for (String line: entity.getComment().split("\n")) {
                    buffer.write( " * " );
                    buffer.write( line );
                    buffer.newline();
                }

                
            }

            if ( entity.getTags().size() > 0 )
            {
                if ( entity.getComment() != null && entity.getComment().length() > 0 )
                {
                    buffer.write( " *" );
                    buffer.newline();
                }
                for ( DocletTag docletTag : entity.getTags() )
                {
                    buffer.write( " * @" );
                    buffer.write( docletTag.getName() );
                    if ( docletTag.getValue().length() > 0 )
                    {
                        buffer.write( ' ' );
                        buffer.write( docletTag.getValue() );
                    }
                    buffer.newline();
                }
            }

            buffer.write( " */" );
            buffer.newline();
        }
        if ( entity.getAnnotations() != null )
        {
            for ( JavaAnnotation annotation : entity.getAnnotations() )
            {
                writeAnnotation( annotation );
            }
        }
    }

    @Override
    public String toString()
    {
        return buffer.toString();
    }
}