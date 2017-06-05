package ir.university.toosi.tms.model.entity;


public class PersonSearch extends BaseEntity {

    private String preCondition;
    private String attributeName;
    private String attributeValue;
    private String postCondition;

    public PersonSearch() {
    }

    public PersonSearch(String preCondition, String attributeName, String attributeValue, String postCondition) {
        this.preCondition = preCondition;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.postCondition = postCondition;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getPostCondition() {
        return postCondition;
    }

    public void setPostCondition(String postCondition) {
        this.postCondition = postCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonSearch that = (PersonSearch) o;

        if (attributeName != null ? !attributeName.equals(that.attributeName) : that.attributeName != null)
            return false;
        if (attributeValue != null ? !attributeValue.equals(that.attributeValue) : that.attributeValue != null)
            return false;
        if (postCondition != null ? !postCondition.equals(that.postCondition) : that.postCondition != null)
            return false;
        if (preCondition != null ? !preCondition.equals(that.preCondition) : that.preCondition != null) return false;

        return true;
    }
}