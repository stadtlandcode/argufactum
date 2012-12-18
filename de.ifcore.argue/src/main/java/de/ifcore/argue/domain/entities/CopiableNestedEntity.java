package de.ifcore.argue.domain.entities;

public interface CopiableNestedEntity<T, P>
{
	public T copyTo(P destionationParent, int timeDiff);
}
