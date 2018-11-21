package org.gsc.account;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.IOException;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

/**
 * @Auther: kay
 * @Date: 11/21/18 16:24
 * @Description:
 */
public class AccountTest {

    public static void main(String[] args) {

        Options options = new Options();
        options.createIfMissing(true);
        DB db = null;
        try {
            db = factory.open(new File("/home/kay/workspace/mico/source/gsc-script/database/account"), options);

            System.out.println();
            DBIterator iterator = db.iterator();
            iterator.seekToFirst();
            while (iterator.hasNext()) {
                String keyStr = Hex.toHexString(iterator.peekNext().getKey());
                String valueStr = Hex.toHexString(iterator.peekNext().getValue());

                System.out.println("key:" + keyStr + ", value:" + valueStr);
                iterator.next();
            }
            iterator.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
