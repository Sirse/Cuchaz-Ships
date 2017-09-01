/*******************************************************************************
 * Copyright (c) 2014 jeff.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     jeff - initial API and implementation
 ******************************************************************************/
package cuchaz.modsShared.blocks;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class BlockSetProxy extends BlockSet {
	
	private static final long serialVersionUID = -4883453586052528819L;
	
	private Set<Coords> m_coords;
	
	public BlockSetProxy(Set<Coords> coords) {
		super();
		m_coords = coords;
	}
	
	@Override
	public boolean add(Coords val) {
		return m_coords.add(val);
	}
	
	@Override
	public void clear() {
		m_coords.clear();
	}
	
	@Override
	public boolean contains(Object val) {
		return m_coords.contains(val);
	}
	
	@Override
	public boolean isEmpty() {
		return m_coords.isEmpty();
	}
	
	@Override
	public Iterator<Coords> iterator() {
		return m_coords.iterator();
	}
	
	@Override
	public boolean remove(Object val) {
		return m_coords.remove(val);
	}
	
	@Override
	public int size() {
		return m_coords.size();
	}
	
	@Override
	public boolean equals(Object val) {
		return m_coords.equals(val);
	}
	
	@Override
	public int hashCode() {
		return m_coords.hashCode();
	}
	
	@Override
	public boolean removeAll(Collection<?> val) {
		return m_coords.removeAll(val);
	}
	
	@Override
	public boolean addAll(Collection<? extends Coords> val) {
		return m_coords.addAll(val);
	}
	
	@Override
	public boolean containsAll(Collection<?> val) {
		return m_coords.containsAll(val);
	}
	
	@Override
	public boolean retainAll(Collection<?> val) {
		return m_coords.retainAll(val);
	}
	
	@Override
	public Object[] toArray() {
		return m_coords.toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] val) {
		return m_coords.toArray(val);
	}
}
