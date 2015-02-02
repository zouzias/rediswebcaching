package org.zouzias.rediscachingfilter.tests;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author zouzias
 */
public class RedisFilterCommons {
    public static String stringifyHttpPostRequest(String URL, String content) {
            return URL + ":" + DigestUtils.md5Hex(content);
    }
}
