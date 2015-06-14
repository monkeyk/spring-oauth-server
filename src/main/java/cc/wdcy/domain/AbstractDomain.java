package cc.wdcy.domain;

import cc.wdcy.infrastructure.DateUtils;
import cc.wdcy.domain.shared.GuidGenerator;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Shengzhao Li
 */
public abstract class AbstractDomain implements Serializable {

    /**
     * Database id
     */
    protected int id;

    protected boolean archived;
    /**
     * Domain business guid.
     */
    protected String guid = GuidGenerator.generate();

    /**
     * The domain create time.
     */
    protected Date createTime = DateUtils.now();

    public AbstractDomain() {
    }

    public int id() {
        return id;
    }

    public void id(int id) {
        this.id = id;
    }

    public boolean archived() {
        return archived;
    }

    public AbstractDomain archived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public String guid() {
        return guid;
    }

    public void guid(String guid) {
        this.guid = guid;
    }

    public Date createTime() {
        return createTime;
    }

    public boolean isNewly() {
        return id == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDomain)) {
            return false;
        }
        AbstractDomain that = (AbstractDomain) o;
        return guid.equals(that.guid);
    }

    @Override
    public int hashCode() {
        return guid.hashCode();
    }

    //For subclass override it
    public void saveOrUpdate() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{id=").append(id);
        sb.append(", archived=").append(archived);
        sb.append(", guid='").append(guid).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}