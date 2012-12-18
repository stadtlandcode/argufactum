package de.ifcore.argue.domain.enumerations;

public enum Relevance
{
	LOW(1), AVERAGE(2), HIGH(3);

	private Byte byteValue;

	private Relevance(int byteValue)
	{
		this.byteValue = Byte.valueOf((byte)byteValue);
	}

	public Byte getByteValue()
	{
		return byteValue;
	}

	public static Relevance forByteValue(Byte byteValue)
	{
		for (Relevance relevance : Relevance.values())
		{
			if (relevance.getByteValue().equals(byteValue))
				return relevance;
		}
		return null;
	}
}
