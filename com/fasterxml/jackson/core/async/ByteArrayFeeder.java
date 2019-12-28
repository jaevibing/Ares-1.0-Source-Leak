package com.fasterxml.jackson.core.async;

import java.io.IOException;

public interface ByteArrayFeeder extends NonBlockingInputFeeder
{
    void feedInput(byte[] p0, int p1, int p2) throws IOException;
}
