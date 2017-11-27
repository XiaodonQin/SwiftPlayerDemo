package sg.com.conversant.swiftplayer.feedback;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public class Header {
    private final String name;
    private final String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            Header header = (Header)o;
            if(this.name != null) {
                if(!this.name.equals(header.name)) {
                    return false;
                }
            } else if(header.name != null) {
                return false;
            }

            boolean var10000;
            label51: {
                if(this.value != null) {
                    if(!this.value.equals(header.value)) {
                        break label51;
                    }
                } else if(header.value != null) {
                    break label51;
                }

                var10000 = true;
                return var10000;
            }

            var10000 = false;
            return var10000;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.name != null?this.name.hashCode():0;
        result = 31 * result + (this.value != null?this.value.hashCode():0);
        return result;
    }

    public String toString() {
        return "Header{name=\'" + this.name + '\'' + ", value=\'" + this.value + '\'' + '}';
    }
}
