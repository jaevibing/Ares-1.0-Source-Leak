package com.fasterxml.jackson.core.async;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ByteBufferFeeder extends NonBlockingInputFeeder
{
    void feedInput(ByteBuffer p0) throws IOException;
}
