package cloud.huazai.datatransmission.response;

import cloud.huazai.datatransmission.DTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Response
 *
 * @author devon
 * @since 2024/12/12
 */
@Getter
@Setter
@ToString
public abstract class Response extends DTO {

    private boolean success;

    private String code;

    private String errMessage;

}
