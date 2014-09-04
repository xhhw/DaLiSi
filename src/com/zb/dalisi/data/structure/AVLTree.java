package com.zb.dalisi.data.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * ƽ�������
 * ����:��������һ������Ķ������������������������������������ƽ���������
 * ���������������������֮�����1
 * ƽ������:���Զ���Ϊ����������ȼ�ȥ�����������
 * 
 * ƽ��������ǶԶ������������Ż�����ֹ������������������ƽ������ʱ��Ϊn��
 * �����������ڴ�ʱ����һ��������
 * ƽ�����������Ԫ�صĴ���������������ȣ�ʱ�临�Ӷ�ΪlogN
 */
public class AVLTree<E> {
	/**
	 * ���ڵ�
	 */
	private Entry<E> root = null;
	
	/**
	 * ����Ԫ�ظ���
	 */
	private int size = 0;
	
	public AVLTree(){
		
	}
	
	public int size(){
		return size;
	}


	/**
	 * @param p ��С��ת�����ĸ��ڵ�
	 * ������ת֮��p�Ƶ�p������������p��������B��Ϊ����С�������ڵ㣬
	 * B����������Ϊp��������
	 * ���磺     A(-2)                   B(1)
	 *              \        ����ת       /   \
	 *             B(0)     ---->       A(0)   \       
	 *             /   \                   \    \
	 *           BL(0)  BR(0)              BL(0) BR(0) 
	 *  ��ת֮���������֮�����1
	 */
	private void rotateLeft(Entry<E> p) {
		System.out.println("��"+p.element+"����");
		if(p!=null){
			Entry<E> r = p.right;
			p.right = r.left;	//��p����������ڵ�޽ӵ�p���ҽڵ㣬����ͼ����BL��ΪA�����ӽڵ�
			if (r.left != null)	//���B����ڵ�BL��Ϊ�գ���BL�ĸ��ڵ���ΪA
				r.left.parent = p;
			r.parent = p.parent;  //A�ĸ��ڵ���ΪB�ĸ��ڵ�
			if (p.parent == null)		  //���p�Ǹ��ڵ�
                root = r;				  //r��Ϊ���ڵ㣬��BΪ���ڵ�
            else if (p.parent.left == p)  //���p�����ӽڵ�
                p.parent.left = r;        //p�ĸ��ڵ��������Ϊr
            else					 	  //���p�����ӽڵ�
                p.parent.right = r;		  //p�ĸ��ڵ��������Ϊr
            r.left = p;					  //p��Ϊr������������AΪB��������
            p.parent = r;				  //ͬʱ����p�ĸ��ڵ�Ϊr����A�ĸ��ڵ�ΪB
		}
	}
	
	/**
	 * @param p ��С��ת�����ĸ��ڵ�
	 * ������ת֮��p�Ƶ�p�����ӽڵ㴦��p��������B��Ϊ��С��ת�����ĸ��ڵ�
	 * B�����ӽڵ��Ϊp����ڵ㡢
	 * ����:       A(2)                     B(-1)
	 *            /         ����ת          /    \
	 *          B(0)       ------>         /     A(0)
	 *         /   \                      /      /
	 *       BL(0) BR(0)                BL(0)  BR(0) 
	 */
	private void rotateRight(Entry<E> p){
		System.out.println("��"+p.element+"����");
		if(p!=null){
			Entry<E> l = p.left;  
			p.left = l.right;    //��B���ҽڵ�BR��ΪA����ڵ�
            if (l.right != null)   //���BR��Ϊnull������BR�ĸ��ڵ�ΪA
            	l.right.parent = p;
            l.parent = p.parent;  //A�ĸ��ڵ㸳��B�ĸ��ڵ�
            if (p.parent == null)	//���p�Ǹ��ڵ�
                root = l;          //BΪ���ڵ�
            else if (p.parent.right == p) //���A���丸�ڵ�����ӽڵ�
                p.parent.right = l;		//BΪA�ĸ��ڵ��������
            else 						//���A���丸�ڵ�����ӽڵ�
            	p.parent.left = l;		//BΪA�ĸ��ڵ��������
            l.right = p;				//AΪB��������
            p.parent = l;  				//����A�ĸ��ڵ�ΪB
		}
	}

	
	/**
	 * ƽ����������Ĳ������
	 * ����ԭ��Ϊ��
	 * 1.������ͬ����������һ�㣬�ҵ���Ҫ����Ľڵ��λ�ã�����Ԫ�ز������У�
	 * 2.�������Ͻ��л��ݣ��������������飺
	 * (1)��һ���޸����Ƚڵ��ƽ�����ӣ������� һ���ڵ�ʱֻ�и��ڵ㵽����ڵ�
	 * ��·���еĽڵ��ƽ�����ӻᱻ�ı䣬���Ҹı��ԭ���ǵ�����ڵ���ĳ�ڵ�(��ΪA)
	 * �������� ��ʱ��A��ƽ������(��ΪBF)ΪBF+1,������ڵ���A����������ʱA��BF-1��
	 * ���жϲ���ڵ����������л�����������ֻҪ�򵥵ıȽ�����A�Ĵ�С
	 * (2)�ڸı����Ƚڵ��ƽ�����ӵ�ͬʱ���ҵ����һ��ƽ�����Ӵ���2��С��-2�Ľڵ㣬
	 * ������ڵ㿪ʼ������С��ƽ����������ת������������ε��������ġ�
	 * ���ڵ�������С��ƽ�������ĸ߶������ڵ�ǰ�ĸ߶���ͬ���ʲ������Ҫ�������Ƚڵ㡣
	 * ���ﻹ��һ������������������BFʱ������ĳ���ڵ��BF��Ϊ0�ˣ���ֹͣ���ϼ���������
	 * ��Ϊ������˽ڵ��и߶�С�������������½ڵ㣬�߶Ȳ��䣬��ô���Ƚڵ��BF��Ȼ���䡣
	 * 
	 * 
	 */
	public boolean add(E element){
		Entry<E> t = root;
		if(t == null){
			root = new Entry<E>(element,null);
			size = 1;
			return true;
		}
		int cmp;
		Entry<E> parent;	//����t�ĸ��ڵ�
		Comparable<? super E> e = (Comparable<? super E>) element;
		//�Ӹ��ڵ������������ҵ�����λ��
		do {
			parent = t;		
			cmp = e.compareTo(t.element);
			if(cmp < 0){
				t = t.left;
			}else if(cmp > 0){
				t = t.right;
			}else{
				return false;
			}
		} while (t!=null);
		
		Entry<E> child = new Entry(element,parent);
		if(cmp < 0){
			parent.left = child;
			
		}else{
			parent.right = child;	
		}
		//�������ϻ��ݣ����������ƽ��ڵ�
		while(parent!=null){
			cmp = e.compareTo(parent.element);
			if(cmp < 0){    //����ڵ���parent����������
				parent.balance++;
			}else{			 //����ڵ���parent����������
				parent.balance--;
			}
			if(parent.balance == 0){	//�˽ڵ��balanceΪ0���������ϵ���BFֵ���Ҳ���Ҫ��ת
				break;
			}
			if(Math.abs(parent.balance) == 2){	//�ҵ���С��ƽ���������ڵ�
				fixAfterInsertion(parent);
				break;					//���ü������ϻ���
			}
			parent = parent.parent;
		}
		size ++;
		return true;
	}
	
	/**
	 * �����ķ�����
	 * 1.����С��ƽ�������ĸ�(���¼��R)Ϊ2ʱ����������������������
	 * ���R���������ĸ��ڵ��BFΪ1ʱ����������
	 * ���R���������ĸ��ڵ��BFΪ-1ʱ��������Ȼ��������
	 * 
	 * 2.RΪ-2ʱ����������������������
	 * ���R���������ĸ��ڵ��BFΪ1ʱ��������������
	 * ���R���������ĸ��ڵ��BFΪ-1ʱ��������
	 * 
	 * ���ڵ���֮�󣬸��ڵ��BF�仯������
	 */
	private void fixAfterInsertion(Entry<E> p){
		if(p.balance == 2){
			leftBalance(p);
		}
		if(p.balance == -2){
			rightBalance(p);
		}
	}
	
	
	/**
	 * ����ƽ�⴦��
	 * ƽ�����ӵĵ�����ͼ��
	 *         t                       rd
	 *       /   \                   /    \
	 *      l    tr   ����������    l       t
	 *    /   \       ------->    /  \    /  \
	 *  ll    rd                ll   rdl rdr  tr
	 *       /   \
	 *     rdl  rdr
	 *     
	 *   ���2(rd��BFΪ0)
	 * 
	 *   
	 *         t                       rd
	 *       /   \                   /    \
	 *      l    tr   ����������    l       t
	 *    /   \       ------->    /  \       \
	 *  ll    rd                ll   rdl     tr
	 *       /   
	 *     rdl  
	 *     
	 *   ���1(rd��BFΪ1)
	 *  
	 *   
	 *         t                       rd
	 *       /   \                   /    \
	 *      l    tr   ����������    l       t
	 *    /   \       ------->    /       /  \
	 *  ll    rd                ll       rdr  tr
	 *           \
	 *          rdr
	 *     
	 *   ���3(rd��BFΪ-1)
	 * 
	 *   
	 *         t                         l
	 *       /       ��������          /    \
	 *      l        ------>          ll     t
	 *    /   \                             /
	 *   ll   rd                           rd
	 *   ���4(L�ȸ�)
	 */
	private boolean leftBalance(Entry<E> t){
		boolean heightLower = true;
		Entry<E> l = t.left;
		switch (l.balance) {
		case LH:			//��ߣ���������,��ת�����ĸ߶ȼ�С
			t.balance = l.balance = EH;
			rotateRight(t);
			break; 
		case RH:	        //�Ҹߣ����������											
			Entry<E> rd = l.right;
			switch (rd.balance) {	//���������ڵ��BF
			case LH:	//���1
				t.balance = RH;
				l.balance = EH;
				break;
			case EH:	//���2
				t.balance = l.balance = EH;
				break;
			case RH:	//���3
				t.balance = EH;
				l.balance = LH;
				break;
			}
			rd.balance = EH;
			rotateLeft(t.left);
			rotateRight(t);
			break;
		case EH:	  //�������4,������������ʱ�����ܳ��֣�ֻ���Ƴ�ʱ���ܳ��֣���ת֮���������߲���
			l.balance = RH;
			t.balance = LH;
			rotateRight(t);
			heightLower = false;
			break;
		}
		return heightLower;
	}
	
	/**
	 * ����ƽ�⴦��
	 * ƽ�����ӵĵ�����ͼ��
	 *           t                               ld
	 *        /     \                          /     \
	 *      tl       r       ������������     t       r
	 *             /   \     -------->      /   \    /  \
	 *           ld    rr                 tl   ldl  ldr rr
	 *          /  \
	 *       ldl  ldr
	 *       ���2(ld��BFΪ0)
	 *       
	 *           t                               ld
	 *        /     \                          /     \
	 *      tl       r       ������������     t       r
	 *             /   \     -------->      /   \       \
	 *           ld    rr                 tl   ldl      rr
	 *          /  
	 *       ldl  
	 *       ���1(ld��BFΪ1)
	 *       
	 *           t                               ld
	 *        /     \                          /     \
	 *      tl       r       ������������     t       r
	 *             /   \     -------->      /        /  \
	 *           ld    rr                 tl        ldr rr
	 *             \
	 *             ldr
	 *       ���3(ld��BFΪ-1)
	 *       
	 *           t                                  r
	 *             \          ����                /   \
	 *              r        ------->           t      rr     
	 *            /   \                          \
	 *           ld   rr                         ld
	 *        ���4(r��BFΪ0)   
	 */
	private boolean rightBalance(Entry<E> t){
		boolean heightLower = true;
		Entry<E> r = t.right;
		switch (r.balance) {
		case LH:			//��ߣ����������
			Entry<E> ld = r.left;
			switch (ld.balance) {	//���������ڵ��BF
			case LH:	//���1
				t.balance = EH;
				r.balance = RH;
				break;
			case EH:    //���2
				t.balance = r.balance = EH;
				break;
			case RH:	//���3
				t.balance = LH;
				r.balance = EH;
				break;
			}
			ld.balance = EH;
			rotateRight(t.right);
			rotateLeft(t);
			break;
		case RH:			//�Ҹߣ���������
			t.balance = r.balance = EH;
			rotateLeft(t);
			break;
		case EH:       //�������4
			r.balance = LH;
			t.balance = RH;
			rotateLeft(t);
			heightLower = false;
			break;
		}
		return heightLower;
	}
	
	/**
	 * ����ָ��Ԫ�أ�����ҵ�������Entry���󣬷��򷵻�null
	 */
	private Entry<E> getEntry(Object element) {  
	    Entry<E> tmp = root;  
	    Comparable<? super E> e = (Comparable<? super E>) element;
	    int c;  
	    while (tmp != null) {  
	        c = e.compareTo(tmp.element);  
	        if (c == 0) {  
	            return tmp;  
	        } else if (c < 0) {  
	            tmp = tmp.left;  
	        } else {  
	            tmp = tmp.right;  
	        }  
	    }  
	    return null;  
	}  
	
	/**
	 * ƽ����������Ƴ�Ԫ�ز���
	 * 
	 */
	public boolean remove(Object o){
		Entry<E> e = getEntry(o);
		if(e!=null){
			deleteEntry(e);
			return true;
		}
		return false;
	}
	
	private void deleteEntry(Entry<E> p){
		size --;
		//���p������������Ϊ�գ��ҵ���ֱ�Ӻ�̣��滻p��֮��pָ��s��ɾ��p��ʵ��ɾ��s
		//���е�ɾ������������Ϊ�յĽڵ㶼���Ե���Ϊɾ��������������һ��Ϊ�գ���Ϊ�յ������
		if (p.left != null && p.right != null) {
			 Entry<E> s = successor(p);
			 p.element = s.element;
			 p = s;
		}
		Entry<E> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {		//�����������������һ��Ϊ��
            replacement.parent = p.parent;
            if (p.parent == null)	//���pΪroot�ڵ�
                root = replacement;
            else if (p == p.parent.left)	//���p������
                p.parent.left  = replacement;	
            else							//���p���Һ���
                p.parent.right = replacement;

            p.left = p.right = p.parent = null;		//p��ָ�����
            
            //���������replacement�ĸ��ڵ㣬���Կ���ֱ�Ӵ�����ʼ���ϻ���
            fixAfterDeletion(replacement);	

        } else if (p.parent == null) { // ���ȫ��ֻ��һ���ڵ�
            root = null;
        } else {  //����������Ϊ��
        	fixAfterDeletion(p);	//�����p��ʼ����
            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }	
	}
	
	/**
	 * ���������������ʽ������ʱ��t��ֱ�Ӻ��
	 */
	static <E> Entry<E> successor(Entry<E> t) {
		if (t == null)
			return null;
		else if (t.right != null) {	//���ң�Ȼ������ֱ����ͷ
			Entry<E> p = t.right;
			while (p.left != null)
				p = p.left;
			return p;
		} else {		//rightΪ�գ����t��p������������pΪt��ֱ�Ӻ��
			Entry<E> p = t.parent;
			Entry<E> ch = t;
			while (p != null && ch == p.right) {	//���t��p�������������������������ֱ�Ӻ��
				ch = p;
				p = p.parent;
			}
			return p;
		}
    }
	
	/**
	 * ɾ��ĳ�ڵ�p��ĵ���������
	 * 1.��p��ʼ���ϻ��ݣ��޸����ȵ�BFֵ������ֻҪ������p�ĸ��ڵ㵽���ڵ��BFֵ��
	 * ����ԭ��Ϊ����pλ��ĳ���Ƚڵ�(���A)����������ʱ��A��BF��1����pλ��A��
	 * ��������ʱA��BF��1����ĳ�����Ƚڵ�BF��Ϊ1��-1ʱֹͣ���ݣ�������������෴�ģ�
	 * ��Ϊԭ������ڵ���ƽ��ģ�ɾ������������ĳ���ڵ㲢����ı����ĸ߶�
	 * 
	 * 2.���ÿ���ڵ��BFֵ�����Ϊ2��-2��Ҫ������ת�������������������ģ�
	 * �������֮�������С�����ĸ߶Ƚ����ˣ���ô��������������С�����ĸ��ڵ�(����ΪB)����
	 * ���ϻ��ݣ�����Ͳ��벻һ������ΪB�ĸ��ڵ��ƽ������Ϊ������B�ĸ߶ȵĸı�������˸ı䣬
	 * ��ô�Ϳ�����Ҫ����������ɾ�����ܽ��ж�εĵ�����
	 * 
	 */
	private void fixAfterDeletion(Entry<E> p){
		boolean heightLower = true;		//����С�������������ĸ߶��Ƿ����仯�������С����������
		Entry<E> t = p.parent;
		Comparable<? super E> e = (Comparable<? super E>)p.element;
		int cmp;
		//�������ϻ��ݣ����Ҳ�ƽ��Ľڵ���е���
		while(t!=null && heightLower){
			cmp = e.compareTo(t.element);
			/**
			 * ɾ���Ľڵ��������������ڵĻ�����Ȼ��ɾ����ĳ���ڵ������������Ϊ�յ����
			 * ���磺     10
			 *          /    \
			 *         5     15
			 *       /   \
			 *      3    6 
			 * ����ɾ��5���ǰ�6��ֵ����5��Ȼ��ɾ��6������6��p��p�ĸ��ڵ��ֵҲ��6��
			 * ����Ҳ����������һ��
			 */ 
			if(cmp >= 0 ){   
				t.balance ++;
			}else{
				t.balance --;
			}
			if(Math.abs(t.balance) == 1){	//���ڵ㾭������ƽ�����Ӻ����Ϊ1��-1��˵������֮ǰ��0��ֹͣ���ݡ�
				break;
			}
			Entry<E> r = t;
			//����ĵ���������һ��
			if(t.balance == 2){
				heightLower = leftBalance(r);
			}else if(t.balance==-2){
				heightLower = rightBalance(r);
			}
			t = t.parent;
		}
	}
	
	
	
	private static final int LH = 1;	//���
	private static final int EH = 0;    //�ȸ�
	private static final int RH = -1;	//�Ҹ�
	
	/**
	 * ���ڵ㣬Ϊ���������дget��Set����
	 */
	static class Entry<E>{
		E element;
		Entry<E> parent;
		Entry<E> left;
		Entry<E> right;
		int balance = EH;	//ƽ�����ӣ�ֻ��Ϊ1��0��-1
		
		public Entry(E element,Entry<E> parent){
			this.element = element;
			this.parent = parent;
		}
		
		public Entry(){}

		@Override
		public String toString() {
			return element+" BF="+balance;
		}
			
	}
	
	
	//����������������ĵ�����,���ص���һ�������б�
	private class BinarySortIterator implements Iterator<E>{
		Entry<E> next;
	    Entry<E> lastReturned;
	    
	    public BinarySortIterator(){
	    	Entry<E> s = root;
	    	if(s !=null){
	    		while(s.left != null){	//�ҵ���������ĵ�һ��Ԫ��
	    			s = s.left;
	    		}
	    	}
	    	next = s;	//����next
	    }
	    
		@Override
		public boolean hasNext() {
			return next!=null;
		}

		@Override
		public E next() {
			Entry<E> e = next;
			if (e == null)
				throw new NoSuchElementException();
			next = successor(e);
			lastReturned = e;
			return e.element;
		}

		@Override
		public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            // deleted entries are replaced by their successors
            if (lastReturned.left != null && lastReturned.right != null)
                next = lastReturned;
            deleteEntry(lastReturned);
            lastReturned = null;
		}
	}
	
	public Iterator<E> itrator(){
		return new BinarySortIterator();
	}
	

	
	private int treeHeight(Entry<E> p){
		if(p == null){
			return -1;
		}
		return 1 + Math.max(treeHeight(p.left), treeHeight(p.right));
	}
	
	public boolean containsKey(Entry<E> element){
		Entry<E> t = root;
		if(t == null){
			return false;
		}
		boolean find = false;
		int cmp;
		Entry<E> parent;	//����t�ĸ��ڵ�
		Comparable<? super E> e = (Comparable<? super E>) element;
		//�Ӹ��ڵ������������ҵ�����λ��
		do {
			parent = t;
			cmp = e.compareTo(t.element);
			if(cmp < 0){
				t = t.left;
			}else if(cmp > 0){
				t = t.right;
			}else{
				find = true;
				break;
			}
		} while (t!=null);
		return find;
	}
	
	public E get(Entry<E> element){
		Entry<E> t = root;
		if(t == null){
			return null;
		}
		E rtn = null;
		int cmp;
		Entry<E> parent;	//����t�ĸ��ڵ�
		Comparable<? super E> e = (Comparable<? super E>) element;
		//�Ӹ��ڵ������������ҵ�����λ��
		do {
			parent = t;
			cmp = e.compareTo(t.element);
			if(cmp < 0){
				t = t.left;
			}else if(cmp > 0){
				t = t.right;
			}else{
				rtn = t.element;
				break;
			}
		} while (t!=null);
		return rtn;
	}
	
	
	//���ԣ���Ҳ�����������
	public static void main(String[] args) {
		AVLTree<Integer> tree = new AVLTree<Integer>();
		System.out.println("------���------");
		tree.add(50);
		System.out.print(50+" ");
		tree.add(66);
		System.out.print(66+" ");
		for(int i=0;i<10;i++){
			int ran = (int)(Math.random() * 100);
			System.out.print(ran+" ");
			tree.add(ran);
		}
		System.out.println("------ɾ��------");
		tree.remove(50);
		tree.remove(66);
		
		System.out.println();
		Iterator<Integer> it = tree.itrator();
		while(it.hasNext()){
			System.out.print(it.next()+" ");
		}
	}
}
