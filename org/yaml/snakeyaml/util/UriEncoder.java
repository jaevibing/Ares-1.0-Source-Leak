package org.yaml.snakeyaml.util;

import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.PercentEscaper;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.Charset;
import java.io.UnsupportedEncodingException;
import org.yaml.snakeyaml.error.YAMLException;
import java.net.URLDecoder;
import java.nio.charset.CharacterCodingException;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;
import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.Escaper;
import java.nio.charset.CharsetDecoder;

public abstract class UriEncoder
{
    private static final CharsetDecoder UTF8Decoder;
    private static final String SAFE_CHARS = "-_.!~*'()@:$&,;=[]/";
    private static final Escaper escaper;
    
    public static String encode(final String a1) {
        /*SL:46*/return UriEncoder.escaper.escape(a1);
    }
    
    public static String decode(final ByteBuffer a1) throws CharacterCodingException {
        final CharBuffer v1 = UriEncoder.UTF8Decoder.decode(/*EL:56*/a1);
        /*SL:57*/return v1.toString();
    }
    
    public static String decode(final String v1) {
        try {
            /*SL:62*/return URLDecoder.decode(v1, "UTF-8");
        }
        catch (UnsupportedEncodingException a1) {
            /*SL:64*/throw new YAMLException(a1);
        }
    }
    
    static {
        UTF8Decoder = Charset.forName("UTF-8").newDecoder().onMalformedInput(CodingErrorAction.REPORT);
        escaper = new PercentEscaper("-_.!~*'()@:$&,;=[]/", false);
    }
}
