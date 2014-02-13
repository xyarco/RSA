import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.*;
import javax.swing.*;

public class RSA implements ActionListener{

	JFrame mainJframe;//GUI组件
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
		mainJframe = new JFrame();//部署GUI组件
		con = new Container();
		con = mainJframe.getContentPane();
		mainJframe.setTitle("RSA加密程序");
		
		//Font f = new Font(宋体,50,Font.PLAIN);
		//l1.setFont(f);
		
		l1 = new JLabel("欢迎使用RSA加密程序");
		l2 = new JLabel("请输入密钥长度（512-2048）");
		t0 = new JTextField(6);
		p1 = new JPanel();
		b1 = new JButton("产生密钥");
		b0 = new JButton("显示公钥");
		b2 = new JButton("显示私钥");
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
		r1 = new JRadioButton("输入字串",true);
		r2 = new JRadioButton("选择文件",false);
		bg = new ButtonGroup();
		r1.addActionListener(this);
		r2.addActionListener(this);
		bg.add(r1);bg.add(r2);
		p2.add(r1);
		p2.add(r2);
		
		p3 = new JPanel();
		l3 = new JLabel("输入数据");
		t1 = new JTextField(30);
		p3.add(l3);
		p3.add(t1);
		
		p4 = new JPanel();
		l4 = new JLabel("加密数据");
		t2 = new JTextArea(10,30);
		t2.setEditable(false);
		p4.add(l4);
		p4.add(t2);
		
		p5 = new JPanel();
		l5 = new JLabel("解密数据");
		t3 = new JTextField(30);
		t3.setEditable(false);
		p5.add(l5);p5.add(t3);
		
		p6 = new JPanel();
		b3 = new JButton("加密");
		b4 = new JButton("解密");
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
		
		if(o == b1)//点击了产生密钥按钮，产生公私钥
		{
			a = t0.getText(); //指定密钥的长度，初始化密钥对生成器
			
			if(a == "" || Integer.parseInt(a)<512 || Integer.parseInt(a)>2048){
				JOptionPane.showMessageDialog(mainJframe, "请先输入正确地密钥长度");
				return;
			}
			else
			{
				creatkey(a);
			    JOptionPane.showMessageDialog(mainJframe, "已生成公私钥文件RSA_pub.dat与RSA_priv.dat");
			    mainJframe.validate();	
			}
		}//end if(o == b1)
		
		if(o == b0)//点击显示公钥按钮，显示公钥
		{
			if(a == ""){
				JOptionPane.showMessageDialog(mainJframe, "请先点击产生密钥按钮");
				return;
			}
			else
			{
				readpbkey();//读取公钥
				
				e = pbk.getPublicExponent();//获取公钥的参数e,n
				n = pbk.getModulus();
				 
		        JDialog dialog1 = new JDialog(mainJframe,true);//设置显示公钥对话框
		        dialog1.setSize(400, 400);
		        
		        JTextArea area1 = new JTextArea(10, 10);
		        area1.setText("e="+e.toString()+"\n"+"n="+n.toString());
		        area1.setLineWrap(true);
		        
		        dialog1.add(area1);
		        dialog1.setVisible(true);	
			}
			
			mainJframe.validate();
			
		} //end if(o == b0)
		
		if(o == b2)//点击了显示私钥按钮
		{
			if(t0.getText() == null){
				JOptionPane.showMessageDialog(mainJframe, "请先点击产生密钥按钮");
				return;
			}
			else
			{
				readprikey();//读取私钥
				
				d=prk.getPrivateExponent();//获取私钥的参数d,n
				n=prk.getModulus();
				
		        JDialog dialog2 = new JDialog(mainJframe,true);//设置显示私钥对话框
		        dialog2.setSize(400, 400);
		        
		        JTextArea area2 = new JTextArea(10, 10);
		        area2.setText("d="+d.toString()+"\n"+"n="+n.toString());
		        area2.setLineWrap(true);
		        
		        dialog2.add(area2);
		        dialog2.setVisible(true);
		        
			}//end if(o == b2)
			
			mainJframe.validate();	
		}
		
		
		if(o instanceof JRadioButton)//选择加密的类型，字串或者文件
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
		
		if(o == b3)//加密按钮
		{
			if(r1.isSelected() == true)
			{
				if("".equals(t1.getText()))
				{
					JOptionPane.showMessageDialog(mainJframe, "请输入加密字串");
					return;
				}else
				{
					readpbkey();//读取公钥
					
					String data = t1.getText();
					byte[] plaintext = null;
					try {
						plaintext = data.getBytes("UTF8");
					} catch (UnsupportedEncodingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}//读取的字符存进数组plaintext

					m = new BigInteger(plaintext);
					e = pbk.getPublicExponent();
			        n = pbk.getModulus();
					c = m.modPow(e, n);//加密
					
					t2.setText(String.valueOf(c));//显示
					t2.setLineWrap(true);
		            }
				
				}//end if(r1.isSelected() == true)
				else 
				{
					readpbkey();//读取公钥
					
					byte[] content = new byte[(int)file.length()];
					try{
						RandomAccessFile fp = new RandomAccessFile(file , "rw");
						fp.read(content);
						fp.seek(0);//文件内容存进content数组
						
						m = new BigInteger(content);
						e = pbk.getPublicExponent();
				        n = pbk.getModulus();
						c = m.modPow(e, n);//加密
					
						byte[] ct = c.toByteArray();
						fp.write(ct);
						fp.close();//加密后密文写入文件并关闭
						
						JOptionPane.showMessageDialog(mainJframe, "文件加密成功");
					}catch(Exception e2)
					{
						e2.printStackTrace();
					}	
					
				} 
			mainJframe.validate();
			}//end if(o == b3)

		if(o == b4)//解密按钮
		{
			if(r1.isSelected() == true)
			{
				if("".equals(t1.getText())||"".equals(t2.getText()) )
				{
					JOptionPane.showMessageDialog(mainJframe, "请输入字串加密后再点击");
					return;
				}
				else
				{
					readprikey();//读取私钥
					
					String s = t2.getText();
					c = new BigInteger(s);					
					d=prk.getPrivateExponent();
					n=prk.getModulus();					
					m = c.modPow(d, n);//解密
					
					byte[] mt =m.toByteArray();
					char[] chars = null;
				    for(int i=0;i<mt.length;i++){
				    	String  cx = new String(mt); 
						chars= cx.toCharArray(); 
					   }//解码
					t3.setText(String.valueOf(chars));
				}
			} //end if(r1.isSelected() == true)
			else
			{
				if("".equals(file_n))
				{
					JOptionPane.showMessageDialog(mainJframe, "请选择文件加密后再点击");
					return;
				}
				else
				{
					readprikey();//读取私钥
					
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
						newfp = new RandomAccessFile(newfile , "rw");//读取需解密文件的内容，创建新文件
						
						c = new BigInteger(content);
						d=prk.getPrivateExponent();
						n=prk.getModulus();
						m = c.modPow(d, n);//解密
						
						newfp.write(m.toByteArray());
						newfp.close();//解密后的内容写入文件并关闭
						
						JOptionPane.showMessageDialog(mainJframe, "文件解密成功");
					}
					catch(Exception e2)
					{
						e2.printStackTrace();
					}
				}
			}
			mainJframe.validate();
		}//end of 'if(o == b4)解密按钮'
		
    }
    public static void creatkey(String b){
    	KeyPairGenerator key = null;
    	try {
			key = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e2) {
			 //TODO Auto-generated catch block
			e2.printStackTrace();
		}
		key.initialize(Integer.parseInt(b));//初始化
		KeyPair kp = key.genKeyPair();//生成密钥对
		PublicKey pbkey = kp.getPublic();//获取公钥
		PrivateKey prkey = kp.getPrivate();//获取私钥
		     	
		FileOutputStream f1 = null;//保存公钥到文件
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
		        
		FileOutputStream f2 = null;//保存私钥到文件
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
		
		//获取公钥
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
		}//从文件读取公钥对象后强制转换为 RSAPublicKey 类型，以便后面读取 RSA 算法所需要的参数
		
       return;
    }
	public static void readprikey(){
		
		FileInputStream f = null;
		try {
			f = new FileInputStream("RSA_priv.dat");
		} catch (FileNotFoundException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}//获取私钥
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
		}//从文件读取私钥对象后强制转换为 RSAPublicKey 类型，以便后面读取 RSA 算法所需要的参数
	    return;
    }
}
		
