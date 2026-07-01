package com.alandhaas.urlshortener.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class Base62EncoderTests {

    private final Base62Encoder encoder = new Base62Encoder();

    @Test
    void encodesNumbersAsBase62() {
        assertThat(encoder.encode(0)).isEqualTo("0");
        assertThat(encoder.encode(9)).isEqualTo("9");
        assertThat(encoder.encode(10)).isEqualTo("a");
        assertThat(encoder.encode(35)).isEqualTo("z");
        assertThat(encoder.encode(36)).isEqualTo("A");
        assertThat(encoder.encode(61)).isEqualTo("Z");
        assertThat(encoder.encode(62)).isEqualTo("10");
    }

    @Test
    void rejectsNegativeNumbers() {
        assertThatThrownBy(() -> encoder.encode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Value must not be negative");
    }
}
