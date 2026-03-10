package cloud.huazai.uninotify;

import java.io.Serializable;

/**
 * Builder
 *
 * @author Devon
 * @since 2026/3/9 15:39
 */

public interface  Builder<T> extends Serializable {

    T build();
}
