package org.gsc.module;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Auther: kay
 * @Date: 11/19/18 20:00
 * @Description:
 */
@Setter
@Getter
public class Account implements Serializable {

    private byte[] privateKey;

    private byte[] address;

    public Account() {

    }

    public Account(byte[] privateKey, byte[] address) {
        this.privateKey = privateKey;
        this.address = address;
    }
}
