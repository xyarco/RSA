import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;
import javax.swing.*;

public class RSA implements ActionListener{

	JFrame mainJframe;//GUI���
	Container con;
	JLabel l1,l2,l3,l4,l5;
	JTextField t0,t1,t3;
	JTextArea t2;
	JPanel p1,p2,p3,p4,p5,p6;
	JButton b0,b1,b2,b3,b4;
	JRadioButton r1,r2;
	ButtonGroup bg;
	BigInteger e,n,m,c,d;
	String a;
	File file=null;
	String file_n;
	KeyPairGenerator kpg;
	static RSAPublicKey pbk;
	static RSAPrivateKey prk;

	public RSA()
	{
		mainJframe = new JFrame();//����GUI���
		con = new Container();
		con = mainJframe.getContentPane();
		mainJframe.setTitle("RSA���ܳ���");
		
		//Font f = new Font(����,50,Font.PLAIN);
		//l1.setFont(f);
		
		l1 = new JLabel("��ӭʹ��RSA���ܳ���");
		l2 = new JLabel("��������Կ���ȣ�512-2048��");
		t0 = new JTextField(6);
		p1 = new JPanel();
		b1 = new JButton("������Կ");
		b0 = new JButton("��ʾ��Կ");
		b2 = new JButton("��ʾ˽Կ");
		b1.addActionListener(this);
		b0.addActionListener(this);
		b2.addActionListener(this);
		p1.add(l1);
		p1.add(l2);
		p1.add(t0);
		p1.add(b1);
		p1.add(b0);
		p1.add(b2);
	
		p2 = new JPanel();
		r1 = new JRadioButton("�����ִ�",true);
		r2 = new JRadioButton("ѡ���ļ�",false);
		bg = new ButtonGroup();
		r1.addActionListener(this);
		r2.addActionListener(this);
		bg.add(r1);bg.add(r2);
		p2.add(r1);
		p2.add(r2);
		
		p3 = new JPanel();
		l3 = new JLabel("��������");
		t1 = new JTextField(30);
		p3.add(l3);
		p3.add(t1);
		
		p4 = new JPanel();
		l4 = new JLabel("��������");
		t2 = new JTextArea(10,30);
		t2.setEditable(false);
		p4.add(l4);
		p4.add(t2);
		
		p5 = new JPanel();
		l5 = new JLabel("��������");
		t3 = new JTextField(30);
		t3.setEditable(false);
		p5.add(l5);p5.add(t3);
		
		p6 = new JPanel();
		b3 = new JButton("����");
		b4 = new JButton("����");
		b3.addActionListener(this);
		b4.addActionListener(this);
		p6.add(b3);
		p6.add(b4);
		
		con.setLayout(new FlowLayout());
		con.add(l1);
		con.add(p1);
		con.add(p2);
		con.add(p3);
		con.add(p4);
		con.add(p5);
		con.add(p6);
		
		mainJframe.setVisible(true);
		mainJframe.setBounds(400,200,550,500);
		mainJframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainJframe.validate();
	}
	
	public static void main(String[] args) {
		RSA rsa = new RSA();
	}
	
    public void actionPerformed(ActionEvent e1) {
    	
		Object o = e1.getSource();
		
		if(o == b1)//����˲�����Կ��ť��������˽Կ
		{
			a = t0.getText(); //ָ����Կ�ĳ��ȣ���ʼ����Կ��������
			
			if(a == "" || Integer.parseInt(a)<512 || Integer.parseInt(a)>2048){
				JOptionPane.showMessageDialog(mainJframe, "����������ȷ����Կ����");
				return;
			}
			else
			{
				creatkey(a);
			    JOptionPane.showMessageDialog(mainJframe, "�����ɹ�˽Կ�ļ�RSA_pub.dat��RSA_priv.dat");
			    mainJframe.validate();	
			}
		}//end if(o == b1)
		
		if(o == b0)//�����ʾ��Կ��ť����ʾ��Կ
		{
			if(a == ""){
				JOptionPane.showMessageDialog(mainJframe, "���ȵ��������Կ��ť");
				return;
			}
			else
			{
				readpbkey();//��ȡ��Կ
				
				e = pbk.getPublicExponent();//��ȡ��Կ�Ĳ���e,n
				n = pbk.getModulus();
				 
		        JDialog dialog1 = new JDialog(mainJframe,true);//������ʾ��Կ�Ի���
		        dialog1.setSize(400, 400);
		        
		        JTextArea area1 = new JTextArea(10, 10);
		        area1.setText("e="+e.toString()+"\n"+"n="+n.toString());
		        area1.setLineWrap(true);
		        
		        dialog1.add(area1);
		        dialog1.setVisible(true);	
			}
			
			mainJframe.validate();
			
		} //end if(o == b0)
		
		if(o == b2)//�������ʾ˽Կ��ť
		{
			if(t0.getText() == null){
				JOptionPane.showMessageDialog(mainJframe, "���ȵ��������Կ��ť");
				return;
			}
			else
			{
				readprikey();//��ȡ˽Կ
				
				d=prk.getPrivateExponent();//��ȡ˽Կ�Ĳ���d,n
				n=prk.getModulus();
				
		        JDialog dialog2 = new JDialog(mainJframe,true);//������ʾ˽Կ�Ի���
		        dialog2.setSize(400, 400);
		        
		        JTextArea area2 = new JTextArea(10, 10);
		        area2.setText("d="+d.toString()+"\n"+"n="+n.toString());
		        area2.setLineWrap(true);
		        
		        dialog2.add(area2);
		        dialog2.setVisible(true);
		        
			}//end if(o == b2)
			
			mainJframe.validate();	
		}
		
		
		if(o instanceof JRadioButton)//ѡ����ܵ����ͣ��ִ������ļ�
		{
			if(r1.isSelected() == true)
			{
				t1.setText("");
				t1.setEditable(true);
			}
			else
			{
				t1.setEditable(false);
				
				FileDialog fd = new FileDialog(mainJframe,"file",FileDialog.LOAD);
				fd.setVisible(true);
				file = new File(fd.getDirectory() +"\\"+ fd.getFile());
			}
		}//end if(o instanceof JRadioButton)
		
		if(o == b3)//���ܰ�ť
		{
			if(r1.isSelected() == true)
			{
				if("".equals(t1.getText()))
				{
					JOptionPane.showMessageDialog(mainJframe, "����������ִ�");
					return;
				}else
				{
					readpbkey();//��ȡ��Կ
					
					String data = t1.getText();
					byte[] plaintext = null;
					try {
						plaintext = data.getBytes("UTF8");
					} catch (UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}//��ȡ���ַ��������plaintext

					m = new BigInteger(plaintext);
					e = pbk.getPublicExponent();
			        n = pbk.getModulus();
					c = m.modPow(e, n);//����
					
					t2.setText(String.valueOf(c));//��ʾ
					t2.setLineWrap(true);
		            }
				
				}//end if(r1.isSelected() == true)
				else 
				{
					readpbkey();//��ȡ��Կ
					
					byte[] content = new byte[(int)file.length()];
					try{
						RandomAccessFile fp = new RandomAccessFile(file , "rw");
						fp.read(content);
						fp.seek(0);//�ļ����ݴ��content����
						
						m = new BigInteger(content);
						e = pbk.getPublicExponent();
				        n = pbk.getModulus();
						c = m.modPow(e, n);//����
					
						byte[] ct = c.toByteArray();
						fp.write(ct);
						fp.close();//���ܺ�����д���ļ����ر�
						
						JOptionPane.showMessageDialog(mainJframe, "�ļ����ܳɹ�");
					}catch(Exception e2)
					{
						e2.printStackTrace();
					}	
					
				} 
			mainJframe.validate();
			}//end if(o == b3)

		if(o == b4)//���ܰ�ť
		{
			if(r1.isSelected() == true)
			{
				if("".equals(t1.getText())||"".equals(t2.getText()) )
				{
					JOptionPane.showMessageDialog(mainJframe, "�������ִ����ܺ��ٵ��");
					return;
				}
				else
				{
					readprikey();//��ȡ˽Կ
					
					String s = t2.getText();
					c = new BigInteger(s);					
					d=prk.getPrivateExponent();
					n=prk.getModulus();					
					m = c.modPow(d, n);//����
					
					byte[] mt =m.toByteArray();
					char[] chars = null;
				    for(int i=0;i<mt.length;i++){
				    	String  cx = new String(mt); 
						chars= cx.toCharArray(); 
					   }//����
					t3.setText(String.valueOf(chars));
				}
			} //end if(r1.isSelected() == true)
			else
			{
				if("".equals(file_n))
				{
					JOptionPane.showMessageDialog(mainJframe, "��ѡ���ļ����ܺ��ٵ��");
					return;
				}
				else
				{
					readprikey();//��ȡ˽Կ
					
					byte[] content = new byte[(int)file.length()];
					File newfile;
					RandomAccessFile newfp;
					String path = file.getAbsolutePath();
					try
					{
						RandomAccessFile fp = new RandomAccessFile(file , "rw");
						fp.read(content);
						fp.close();
						file.delete();
						newfile = new File(path);
						newfile.createNewFile();
						newfp = new RandomAccessFile(newfile , "rw");//��ȡ������ļ������ݣ��������ļ�
						
						c = new BigInteger(content);
						d=prk.getPrivateExponent();
						n=prk.getModulus();
						m = c.modPow(d, n);//����
						
						newfp.write(m.toByteArray());
						newfp.close();//���ܺ������д���ļ����ر�
						
						JOptionPane.showMessageDialog(mainJframe, "�ļ����ܳɹ�");
					}
					catch(Exception e2)
					{
						e2.printStackTrace();
					}
				}
			}
			mainJframe.validate();
		}//end of 'if(o == b4)���ܰ�ť'
		
    }
    public static void creatkey(String b){
    	KeyPairGenerator key = null;
    	try {
			key = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e2) {
			 //TODO Auto-generated catch block
			e2.printStackTrace();
		}
		key.initialize(Integer.parseInt(b));//��ʼ��
		KeyPair kp = key.genKeyPair();//������Կ��
		PublicKey pbkey = kp.getPublic();//��ȡ��Կ
		PrivateKey prkey = kp.getPrivate();//��ȡ˽Կ
		     	
		FileOutputStream f1 = null;//���湫Կ���ļ�
		try {
			f1 = new FileOutputStream("RSA_pub.dat");
		} catch (FileNotFoundException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream b1 = null;
		try {
			b1 = new  ObjectOutputStream(f1);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			b1.writeObject(pbkey);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		        
		FileOutputStream f2 = null;//����˽Կ���ļ�
		try {
			f2 = new FileOutputStream("RSA_priv.dat");
		} catch (FileNotFoundException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream b2 = null;
		try {
			b2 = new  ObjectOutputStream(f2);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			b2.writeObject(prkey);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return;
    }
    public static void readpbkey() {
    	FileInputStream f = null;
		try {
			f = new FileInputStream("RSA_pub.dat");
		} catch (FileNotFoundException e2) {
			 //TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//��ȡ��Կ
        ObjectInputStream b = null;
		try {
			b = new ObjectInputStream(f);
		} catch (IOException e2) {
			 //TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			pbk = (RSAPublicKey)b.readObject();
		} catch (Exception e2) {
			 //TODO Auto-generated catch block
			e2.printStackTrace();
		}//���ļ���ȡ��Կ�����ǿ��ת��Ϊ RSAPublicKey ���ͣ��Ա�����ȡ RSA �㷨����Ҫ�Ĳ���
		
       return;
    }
	public static void readprikey(){
		
		FileInputStream f = null;
		try {
			f = new FileInputStream("RSA_priv.dat");
		} catch (FileNotFoundException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}//��ȡ˽Կ
		ObjectInputStream b = null;
		try {
			b = new ObjectInputStream(f);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		try {
			prk = (RSAPrivateKey)b.readObject();
		} catch (Exception e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}//���ļ���ȡ˽Կ�����ǿ��ת��Ϊ RSAPublicKey ���ͣ��Ա�����ȡ RSA �㷨����Ҫ�Ĳ���
	    return;
    }
}
		
