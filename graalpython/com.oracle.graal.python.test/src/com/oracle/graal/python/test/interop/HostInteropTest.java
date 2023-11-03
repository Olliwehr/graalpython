package com.oracle.graal.python.test.interop;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oracle.graal.python.test.PythonTests;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HostInteropTest extends PythonTests {
    private Context context;

    @Before
    public void setUpTest() {
        Context.Builder builder = Context.newBuilder();
        builder.allowExperimentalOptions(true);
        builder.allowAllAccess(true);
        context = builder.build();
    }

    @After
    public void tearDown() {
        context.close();
    }

    @Test
    public void testConstantInteropBehavior() {
        Value t = context.eval("python", """
                        import polyglot

                        class MyType(object):
                            pass

                        polyglot.register_host_interop_behavior(MyType,
                            is_boolean=False,
                            is_number=True,
                            is_string=False,
                            # is_date=False,
                            # is_duration=False,
                            # is_instant=True,
                            # is_iterator=False,
                            # is_time=True,
                            # is_time_zone=False
                        )

                        MyType()
                        """);
        assertFalse(t.isBoolean());
        assertTrue(t.isNumber());
        assertFalse(t.isString());
        // todo (cbasca): implement redefinition of behavior for the following
        // assertFalse(t.isDate());
        // assertFalse(t.isDuration());
        // assertTrue(t.isInstant());
        // assertFalse(t.isIterator());
        // assertTrue(t.isTime());
        // assertFalse(t.isTimeZone());
    }

    @Test
    public void testConstantDefaults() {
        Value t = context.eval("python", """
                        import polyglot

                        class MyType(object):
                            pass

                        polyglot.register_host_interop_behavior(MyType, is_number=True)

                        MyType()
                        """);
        assertFalse(t.isBoolean());
        assertTrue(t.isNumber());
        assertFalse(t.isString());
    }

    @Test
    public void testBoolean() {
        String source = """
                        import polyglot

                        class MyType(object):
                            def __init__(self, data):
                                self._data = data

                        def as_boolean(t):
                            return t._data == "x"

                        polyglot.register_host_interop_behavior(MyType,
                            is_boolean=True,
                            as_boolean=as_boolean
                        )
                        """;
        Value t = context.eval("python", source + "\nMyType('x')");
        assertTrue(t.isBoolean());
        assertTrue(t.asBoolean());
        t = context.eval("python", source + "\nMyType('y')");
        assertTrue(t.isBoolean());
        assertFalse(t.asBoolean());
    }

    @Test
    public void testNumber() {
        String sourceTemplate = """
                        import polyglot

                        class MyType(object):
                            data = %s

                        def get_data(t):
                            return t.data

                        polyglot.register_host_interop_behavior(MyType,
                            is_number=True,
                            fits_in_byte=lambda t: polyglot.fits_in_byte(t.data),
                            fits_in_short=lambda t: polyglot.fits_in_short(t.data),
                            fits_in_int=lambda t: polyglot.fits_in_int(t.data),
                            fits_in_long=lambda t: polyglot.fits_in_long(t.data),
                            fits_in_big_integer=lambda t: polyglot.fits_in_big_integer(t.data),
                            fits_in_float=lambda t: polyglot.fits_in_float(t.data),
                            fits_in_double=lambda t: polyglot.fits_in_double(t.data),
                            as_byte=get_data,
                            as_short=get_data,
                            as_int=get_data,
                            as_long=get_data,
                            as_big_integer=get_data,
                            as_float=get_data,
                            as_double=get_data,
                        )

                        MyType()
                        """;
        Value t;
        // byte
        byte byteValue = (byte) 0x7F;
        t = context.eval("python", String.format(sourceTemplate, byteValue));
        assertTrue(t.isNumber());
        assertTrue(t.fitsInByte());
        assertEquals(byteValue, t.asByte());
        // short
        short shortValue = Short.MAX_VALUE - 1;
        t = context.eval("python", String.format(sourceTemplate, shortValue));
        assertTrue(t.isNumber());
        assertFalse(t.fitsInByte());
        assertTrue(t.fitsInShort());
        assertEquals(shortValue, t.asShort());
        // int
        int intValue = Integer.MAX_VALUE - 1;
        t = context.eval("python", String.format(sourceTemplate, intValue));
        assertTrue(t.isNumber());
        assertFalse(t.fitsInByte());
        assertFalse(t.fitsInShort());
        assertTrue(t.fitsInInt());
        assertEquals(intValue, t.asInt());
        // long
        long longValue = Long.MAX_VALUE - 1;
        t = context.eval("python", String.format(sourceTemplate, longValue));
        assertTrue(t.isNumber());
        assertFalse(t.fitsInByte());
        assertFalse(t.fitsInShort());
        assertFalse(t.fitsInInt());
        assertTrue(t.fitsInLong());
        assertEquals(longValue, t.asLong());
        // big integer
        BigInteger bigInteger = new BigInteger("9223372036854775807123456789", 10);
        t = context.eval("python", String.format(sourceTemplate, bigInteger));
        assertTrue(t.isNumber());
        assertFalse(t.fitsInByte());
        assertFalse(t.fitsInShort());
        assertFalse(t.fitsInInt());
        assertFalse(t.fitsInLong());
        assertTrue(t.fitsInBigInteger());
        assertEquals(bigInteger, t.asBigInteger());
        // float
        float floatValue = 0.5f;
        String floatAsString = "0.5";
        t = context.eval("python", String.format(sourceTemplate, floatAsString));
        assertTrue(t.isNumber());
        assertFalse(t.fitsInByte());
        assertFalse(t.fitsInShort());
        assertFalse(t.fitsInInt());
        assertFalse(t.fitsInLong());
        assertTrue(t.fitsInFloat());
        assertEquals(floatValue, t.asFloat(), 0);
        // double
        double doubleValue = 123.45678901234;
        String doubleAsString = "123.45678901234";
        t = context.eval("python", String.format(sourceTemplate, doubleAsString));
        assertTrue(t.isNumber());
        assertFalse(t.fitsInByte());
        assertFalse(t.fitsInShort());
        assertFalse(t.fitsInInt());
        assertFalse(t.fitsInLong());
        assertFalse(t.fitsInFloat());
        assertTrue(t.fitsInDouble());
        assertEquals(doubleValue, t.asDouble(), 0);
    }

    @Test
    public void testString() {
        Value t = context.eval("python", """
                        import polyglot

                        class MyType(object):
                            data = 10

                        polyglot.register_host_interop_behavior(MyType,
                            is_string=True,
                            as_string=lambda t: f"MyType({t.data})"
                        )

                        MyType()
                        """);
        assertFalse(t.isBoolean());
        assertFalse(t.isNumber());
        assertTrue(t.isString());
        assertEquals("MyType(10)", t.asString());
    }
}
