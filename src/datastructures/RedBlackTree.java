/*
 * Data Structures and Algorithms.
 * Copyright (C) 2016 Rafael Guterres Jeffman
 *
 * See the LICENSE file accompanying this source code, for
 * licensing restrictions that might apply.
 *
 */

package datastructures;

class RedBlackNode<T extends Comparable<T>> {

	private T value;
	private RedBlackNode<T> left;
	private RedBlackNode<T> right;
	private RedBlackNode<T> parent;
	private boolean red;
	
	public RedBlackNode(T value) {
		this.value = value;
		this.red = true;
	}

	public RedBlackNode<T> insert(T value) throws DuplicateKeyException {
		int cmp = value.compareTo(this.value);
		if (cmp < 0)
			return insertLeft(value);
		else if (cmp > 0)
			return insertRight(value);
		else
			throw new DuplicateKeyException("Already inserted: "+value);
	}
	
	
	private RedBlackNode<T> insertLeft(T value) throws DuplicateKeyException {
		if (left == null) {
			left = new RedBlackNode<>(value);
			left.parent = this;
			return left;
		} else
			return left.insert(value);
	}

	private RedBlackNode<T> insertRight(T value) throws DuplicateKeyException {
		if (right == null) {
			right = new RedBlackNode<>(value);
			right.parent = this;
			return right;
		} else
			return right.insert(value);
	}
	
	public RedBlackNode<T> getParent() {
		return parent;
	}
	
	public void setBlack() {
		this.red = false;
	}

	public void setRed() {
		this.red = true;
	}

	public boolean isRed() {
		return red;
	}
	
	public void print() {
		String r = red ? "*" : ""; 
		System.out.print("(" + value + r +" ");
		if (left != null)
			left.print();
		else
			System.out.print("_");
		if (right != null)
			right.print();
		else
			System.out.print(" _");
		System.out.print(")");
	}

	public RedBlackNode<T> getUncle() {
		if (parent == null)
			return null;
		RedBlackNode<T> G = parent.parent;
		if (G == null)
			return null;
		if (G.left == parent)
			return G.right;
		return G.left;
	}

	public boolean isRightSon() {
		if (parent == null)
			return false;
		return parent.right == this;
	}

	public boolean isLeftSon() {
		if (parent == null)
			return false;
		return parent.left == this;
	}
	
	public void rotateLeft() {
		if (right == null) return;
		RedBlackNode<T> N = this;
		RedBlackNode<T> P = this.parent;
		RedBlackNode<T> R = this.right;
		RedBlackNode<T> S = R.left;
		//
		RedBlackNode<T> B = S;
		R.left = N;
		N.right = B;
		// parents
		N.parent = R;
		R.parent = P;
		if (B != null)
			B.parent = N;
	}

	public void rotateRight() {
		if (left == null) return;
		RedBlackNode<T> N = this;
		RedBlackNode<T> P = this.parent;
		RedBlackNode<T> L = this.left;
		RedBlackNode<T> S = L.right;
		//
		RedBlackNode<T> B = S;
		L.right = N;
		N.left = B;
		// parents
		N.parent = L;
		L.parent = P;
		if (B != null)
			B.parent = N;
	}

	public void setRight(RedBlackNode<T> node) {
		right = node;
	}

	public void setLeft(RedBlackNode<T> node) {
		left = node;
	}
	
	public RedBlackNode getLeft() {
		return this.left;
	}
	
	public RedBlackNode getRight() {
		return this.right;
	}
	
	public boolean replaceNode(RedBlackNode newNode) {
		newNode.left = this.left;
		newNode.right = this.right;
		newNode.value = this.value;
		newNode.parent = this.parent;
	}
}

public class RedBlackTree<T extends Comparable<T>>
{
	private RedBlackNode<T> root;
	
	public void insert(T data) throws DuplicateKeyException
	{
		RedBlackNode<T> node;
		if (root == null)
			node = root = new RedBlackNode<>(data);
		else
			node = root.insert(data);
		insert_case1(node);
	}
	
	private void insert_case1(RedBlackNode<T> node) {
		if (node.getParent() == null) {
			node.setBlack();
			return;
		}
		insert_case2(node);
	}
	
	private void insert_case2(RedBlackNode<T> node) {
		RedBlackNode<T> P = node.getParent();
		if (!P.isRed()) return;
		insert_case3(node);
	}
	
	private void insert_case3(RedBlackNode<T> node) {
		RedBlackNode<T> P = node.getParent();
		RedBlackNode<T> U = node.getUncle();
		RedBlackNode<T> G = P.getParent();
		if (P.isRed() && (U != null && U.isRed())) {
			P.setBlack();
			U.setBlack();
			G.setRed();
			insert_case1(G);
		} else
			insert_case4(node);
	}
	
	private void insert_case4(RedBlackNode<T> node) { // P is R, U is B 
		RedBlackNode<T> P = node.getParent();
		RedBlackNode<T> G = P.getParent();
		RedBlackNode<T> N = node;

		if (P.isRightSon() && !node.isRightSon()) {
			P.rotateRight();
			G.setRight(node);
			N = P;
		}
		else if (P.isLeftSon() && !node.isLeftSon()) {
			P.rotateLeft();
			G.setLeft(node);
			N = P;
		}
			
		insert_case5(N);
	}
	
	private void insert_case5(RedBlackNode<T> node) { // P is R, U is B 
		RedBlackNode<T> P = (RedBlackNode<T>)(node.getParent());
		RedBlackNode<T> G = (RedBlackNode<T>)P.getParent();
		RedBlackNode<T> GG = null;
		boolean gl = G.isLeftSon(); 
		if (G.getParent() != null)
			GG = (RedBlackNode<T>)(G.getParent());
		
		P.setBlack();
		G.setRed();
		if (P.isRightSon()) {
			G.rotateLeft();
		} else {
			G.rotateRight();
		}
		if (GG != null)
			if (gl) GG.setLeft(P);
			else GG.setRight(P);
		else
			root = P;
	}
	
	
	private RedBlackNode brotherNode(RedBlackNode n) {
		if (n.getParent().isLeftSon()) {
			return n.getParent().getRight();
		} else {
			return n.getParent().getLeft();
		}
	}

	
	/**
	 * Node n has at most one child
	 */
	public void excludeSon(RedBlackNode n) {
		RedBlackNode son;
		if (n.getLeft() != null) {
			son = n.getLeft();
		} else {
			son = n.getRight();
		}
		n.replaceNode(son);
		if (!n.isRed()) {
			if (son.isRed()) {
				son.setBlack();
			} else {
				exclude_case1(son);
			}
		}
	}
	
	public void exclude_case1(RedBlackNode n) {
		if (n.getParent() != null) {
			// it's not root
			exclude_cas2(n);
		}
	}
	
	public void exclude_case2(RedBlackNode n) {
		RedBlackNode b = this.brotherNode(n);
		if (b.isRed()) {
			n.getParent().setRed();
			b.setBlack();
			if (n == n.getParent().getLeft()) {
				n.getParent().rotateLeft();
			} else {
				n.getParent().rotateRight();
			}
		}
		exclude_case3(n);
	}
	
	public void exclude_case3(RedBlackNode n) {
		RedBlackNode b = this.brotherNode(n);
		if ( !n.getParent().isRed() &&
			 !b.isRed() &&
			 !b.getLeft().isRed() &&
			 !b.getRight().isRed()) {
			b.setRed();
			exclude_case1(n.getParent());
		} else {
			exclude_case4(n);
		}
	}
	
	public void exclude_case4(RedBlackNode n) {
		RedBlackNode b = this.brotherNode(n);
		if ( n.getParent().isRed() &&
			 !b.isRed() &&
			 !b.getLeft().isRed() && 
			 !b.getRight().isRed() ) {
			b.setRed();
			n.getParent().setBlack();
		} else {
			exclude_case5(n);
		}
	}
	
	public void exclude_case5(RedBlackNode n) {
		RedBlackNode b = this.brotherNode(n);
		if (!b.isRed()) {
			if ( n == n.getParent().getLeft() &&
				 !b.getRight().isRed() && 
				 b.getRight().isRed()) {
				b.setRed();
				b.getLeft().setBlack();
				b.rotateRight();
			} else if (n == n.getParent().getRight() && 
					   !b.getLeft().isRed() &&
					   b.getRight().isRed()) {
				b.setRed();
				b.getRight().setBlack();
				b.rotateLeft();
			}
		}
		exclude_case6(n);
	}
	
	public void exclude_case6(RedBlackNode n) {
		RedBlackNode b = this.brotherNode(n);
		if (n.getParent().isRed()) {
			b.setRed();
		}
		if (n.getParent().isBlack()) {
			b.setBlack();
		}
		n.getParent().setBlack();
		
		if (n == n.getParent().getLeft()) {
			b.getRight().setBlack();
			n.getParent().rotateLeft();
		} else {
			b.getLeft().setBlack();
			n.getParent().rotateRight();
		}
	}
	
	public void print() {
		if (root == null)
			System.out.println("Empty tree.");
		else
			root.print();
	}
}
