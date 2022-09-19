package cloud.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Size {
    XS("XS"),
    S("S"),
    M("M"),
    L("L"),
    XL("XL");

    private final String size;
}