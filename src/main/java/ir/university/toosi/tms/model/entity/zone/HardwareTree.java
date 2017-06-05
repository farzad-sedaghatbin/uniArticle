package ir.university.toosi.tms.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;

/**
 * Created by Farzad on 4/19/2014.
 */
public abstract class HardwareTree extends BaseEntity implements javax.swing.tree.TreeNode {
    @JsonProperty
    protected String name;
    @JsonIgnore
    protected String type;
}
