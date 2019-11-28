import org.junit.Test;
import utils.EncryptUtil;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;

/**
 * @author junan
 * @version V1.0
 * @className EncrypTest
 * @disc
 * @date 2019/11/27 14:40
 */
public class EncrypTest {

    @Test
    public void test1() {
        String sha256 = EncryptUtil.getSHA512("123456");
        System.out.println(sha256);
    }

    @Test
    public void test2() {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(provider.getName() + "  " + provider.getVersion());
        }
    }

}