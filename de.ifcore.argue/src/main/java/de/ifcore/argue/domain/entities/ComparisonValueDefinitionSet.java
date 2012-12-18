package de.ifcore.argue.domain.entities;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import de.ifcore.argue.domain.enumerations.ComparisonValueInputMethod;
import de.ifcore.argue.domain.enumerations.CriterionDataType;
import de.ifcore.argue.domain.enumerations.SortOrder;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionReport;
import de.ifcore.argue.domain.report.ComparisonValueDefinitionSetReport;

@Entity
public class ComparisonValueDefinitionSet extends AbstractAuthoredEntity implements IdEntity,
		Comparable<ComparisonValueDefinitionSet>, ComparisonValueDefinitionSetReport
{
	private static final long serialVersionUID = -7436385344380030556L;
	private static final ComparisonValueNumberDefinition defaultNumberDefinition = new ComparisonValueNumberDefinition(
			null, null, SortOrder.DESC);

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private CriterionDataType dataType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private ComparisonValueInputMethod inputMethod;

	private String name;

	private Integer sortKey;

	@Column(updatable = false, name = "is_global")
	private Boolean global;

	@OneToMany(mappedBy = "definitionSet")
	private Set<Criterion> criteria;

	@OneToMany(mappedBy = "definitionSet", fetch = FetchType.EAGER)
	@Sort(type = SortType.NATURAL)
	private SortedSet<ComparisonValueDefinition> definitions;

	private ComparisonValueNumberDefinition numberDefinition;

	ComparisonValueDefinitionSet()
	{
	}

	public ComparisonValueDefinitionSet(CriterionDataType dataType, String name, Author author)
	{
		super(author);
		if (dataType == null)
			throw new IllegalArgumentException();
		this.dataType = dataType;
		this.name = name;
		this.inputMethod = guessInputMethod(dataType);
	}

	private ComparisonValueDefinitionSet(CriterionDataType dataType, ComparisonValueInputMethod inputMethod,
			String name, Integer sortKey, boolean global)
	{
		this(dataType, name, null);
		this.global = Boolean.valueOf(global);
		this.sortKey = sortKey;
		if (inputMethod != null)
			this.inputMethod = inputMethod;
	}

	public static ComparisonValueDefinitionSet maintainedSet(CriterionDataType dataType,
			ComparisonValueInputMethod inputMethod, String name, Integer sortKey, boolean global)
	{
		return new ComparisonValueDefinitionSet(dataType, inputMethod, name, sortKey, true);
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public CriterionDataType getDataType()
	{
		return dataType;
	}

	@Override
	public ComparisonValueInputMethod getInputMethod()
	{
		return inputMethod;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isGlobal()
	{
		return BooleanUtils.toBoolean(global);
	}

	@Override
	public Integer getSortKey()
	{
		return sortKey == null ? Integer.valueOf(0) : sortKey;
	}

	public Set<Criterion> getCriteria()
	{
		return criteria == null ? Collections.<Criterion> emptySet() : Collections.unmodifiableSet(criteria);
	}

	public Set<ComparisonValueDefinition> getDefinitions()
	{
		return definitions == null ? Collections.<ComparisonValueDefinition> emptySet() : Collections
				.unmodifiableSet(definitions);
	}

	@Override
	public Set<ComparisonValueDefinitionReport> getDefinitionReports()
	{
		return definitions == null ? Collections.<ComparisonValueDefinitionReport> emptySet() : Collections
				.<ComparisonValueDefinitionReport> unmodifiableSet(definitions);
	}

	void addDefinition(ComparisonValueDefinition definition)
	{
		if (definition == null || definition.getDefinitionSet() != this)
			throw new IllegalArgumentException();
		if (CriterionDataType.NUMBER.equals(dataType))
			throw new UnsupportedOperationException();
		if (definitions == null)
			definitions = new TreeSet<>();
		definitions.add(definition);
	}

	private ComparisonValueInputMethod guessInputMethod(CriterionDataType dataType)
	{
		if (CriterionDataType.RATING.equals(dataType))
			return ComparisonValueInputMethod.SELECT;
		else if (CriterionDataType.BOOLEAN.equals(dataType))
			return ComparisonValueInputMethod.CHECKBOX;
		else
			return ComparisonValueInputMethod.TEXT;
	}

	@Override
	public ComparisonValueNumberDefinition getNumberDefinition()
	{
		if (!CriterionDataType.NUMBER.equals(dataType))
			return null;
		else
			return numberDefinition == null ? defaultNumberDefinition : numberDefinition;
	}

	public void setNumberDefinition(BigDecimal min, BigDecimal max, SortOrder sortOrder)
	{
		if (!CriterionDataType.NUMBER.equals(dataType))
			throw new UnsupportedOperationException();
		this.numberDefinition = new ComparisonValueNumberDefinition(min, max, sortOrder);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof ComparisonValueDefinitionSet))
			return false;

		final ComparisonValueDefinitionSet rhs = (ComparisonValueDefinitionSet)obj;
		return new EqualsBuilder().append(getDataType(), rhs.getDataType())
				.append(getInputMethod(), rhs.getInputMethod()).append(getSortKey(), rhs.getSortKey())
				.append(getName(), rhs.getName()).append(getAuthor(), rhs.getAuthor())
				.append(getCreatedAt(), rhs.getCreatedAt()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(23, 31).append(getDataType()).append(getInputMethod()).append(getSortKey())
				.append(getName()).append(getAuthor()).append(getCreatedAt()).toHashCode();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ComparisonValueDefinitionSet [id=").append(id).append(", dataType=").append(dataType)
				.append(", name=").append(name).append(", sortKey=").append(sortKey).append(", global=").append(global)
				.append(", inputMethod=").append(inputMethod).append(", author=").append(getAuthor())
				.append(", createdAt=").append(getCreatedAt()).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(ComparisonValueDefinitionSet o)
	{
		if (this.equals(o))
			return 0;

		int result = getDataType().compareTo(o.getDataType());
		if (result == 0)
			result = BooleanUtils.toIntegerObject(isGlobal()).compareTo(BooleanUtils.toIntegerObject(isGlobal()));
		if (result == 0)
			result = o.getSortKey().compareTo(getSortKey());
		if (result == 0)
			result = getCreatedAt().compareTo(o.getCreatedAt());
		if (result == 0)
			result = getName().compareTo(o.getName());
		if (result == 0)
			result = getId().compareTo(o.getId());
		return result;
	}
}
