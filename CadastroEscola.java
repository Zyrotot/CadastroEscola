import java.io.*;
import java.util.*;

	public class CadastroEscola {
		public static void main(String[] args)  throws Exception {
		//lista de arquivos a se acessar
			List<String> files = Arrays.asList("./turma1.csv", "./turma2.csv", "./turma3.csv");

		//acessa o arquivo
			for (String strFile : files){
				File file = new File(strFile); 
				BufferedReader bufferFile = new BufferedReader(new FileReader(file)); 

		//cria variáveis utilizdas na leitura
				String linhaSt;
				String linhaSplit[] = new String[10];

		// corre o cabeçalho do arquivo
				linhaSt = bufferFile.readLine();
				linhaSt = bufferFile.readLine();
				linhaSplit = linhaSt.split(";");

		//cria a turma
				Turma turma = new Turma(linhaSplit[1],linhaSplit[2], linhaSplit[5], linhaSplit[7], linhaSplit[8], linhaSplit[9]);

		//preenche a turma
				while ((linhaSt = bufferFile.readLine()) != null) {
					linhaSplit = linhaSt.split(";");
					int matricula = Integer.parseInt(linhaSplit[1]);
					switch (linhaSplit[0]){
					case "aluno":	
					Aluno aluno;
					if 	(Aluno.lista.containsKey(matricula)){
						aluno = Aluno.lista.get(matricula);						
					} else {
						aluno = new Aluno(matricula,linhaSplit[2],linhaSplit[3],linhaSplit[4],linhaSplit[5],linhaSplit[6]);		
					}
					aluno.addTurma(turma);
				    break;
				    case "professor":
				    Professor professor;
				    if 	(Professor.lista.containsKey(matricula)){
				    	professor = Professor.lista.get(matricula);
				    } else {
				    	professor = new Professor(matricula,linhaSplit[2],linhaSplit[3],linhaSplit[4],linhaSplit[5],linhaSplit[7]);
				    }
					professor.addTurma(turma);
					break;
					default:
					System.out.println("ERRO: Tipo \" "+linhaSplit[0]+" \" não suportado!");
					break;}
					
				}
				Turma.lista.put(turma.getCodigo(),turma);
			}




			//Rotina de Teste
			Turma.lista.get("DAS001").info();
			System.out.println("\n-----\n");
			Aluno.lista.get(100).info();
			System.out.println("\n-----\n");
			Professor.lista.get(000).info();
			System.out.println("\n-----\n");
			Turma.lista.get("DAS002").listaParticipantes();
			System.out.println("\n-----\n");
			Aluno.lista.get(101).atividades(2019);
			System.out.println("\n-----\n");
			Professor.lista.get(003).atividades(2019);

			
		}
	}

//classe abstrata de pessoa
abstract class Pessoa {
	//inicializa variaveis da classe
	protected String nome;
	protected char genero;
	protected String email;
	protected int anoNasc;
	protected int matricula;
	protected List<Turma> turmas = new ArrayList<Turma>();

	//inicializa lista de pessoas cadastradas
	static Hashtable lista;

	//Construtor de classe
	Pessoa (int m, String n, String g, String e, String b){
		matricula = m;
		nome = n;
		genero = g.charAt(0);
		email = e;
		anoNasc = Integer.parseInt(b);
	}

	//getter para a matricula de uma pessoa
	int getMatricula(){
		return matricula;
	}

	//getter para nome de uma pessoa
	String getNome(){
		return nome;
	}

	//adiciona turma à pessoa
	void addTurma(Turma t){
		t.addParticipante(this);
		turmas.add(t);
	}

	//mostra infos pessoais básicas
	void info() {
		System.out.println("Name: "+nome);
		System.out.println("Matrícula: "+matricula); 
		System.out.println("M/F/X: "+genero); 
		System.out.println("Ano de Nascimento: "+anoNasc); 
		System.out.println("Email: "+email); 
	}

	//mostra turmas nas quais uma pessoa está cadastrada
	void atividades(int anoConsulta){
		int ha = 0;
		int m = 0;

		System.out.println("Disciplinas de "+nome+" em "+anoConsulta+":");
		for(Turma k : turmas){
			if (anoConsulta == k.getAno()) {
				System.out.println(k.getCodigo() + " - " + k.getNome() + " - " + k.getHA() + "H.A.");
				ha += k.getHA();
				m +=1;
			}
		}
		System.out.println("TOTAL - " + m + " Matérias - " + ha + "H.A.");
	}
	
	//getter para lista de pessoas
	Hashtable getLista(){
		return lista;
	}
}

//classe de aluno herda de pessoa
class Aluno extends Pessoa {
	//variaveis do objeto
	private float iaa;	

	//lista de alunos criados
	static Hashtable<Integer, Aluno> lista = new Hashtable<Integer, Aluno>();

    //Construtor de classe
	Aluno (int m, String n, String g, String e, String b, String i){
		super(m, n, g, e, b);
		iaa = Float.parseFloat(i);
		lista.put(m, this);
	}

	//mostra infos de aluno
	void info(){
		super.info();
		System.out.println("IAA: "+iaa);
	}

}

//classe professor
class Professor extends Pessoa {
	//variaveis da classe e lista de objetos criados
	private String areaExpertise = "";
	static Hashtable<Integer, Professor> lista = new Hashtable<Integer, Professor>();
	
	//Construtor de classe
	Professor (int m, String n, String g, String e, String b, String a){
		super(m, n, g, e, b);
		areaExpertise = a;
		lista.put(m, this);
	}

    //mostra as infos da pessoa
	void info() {
		super.info();
		System.out.println("Area de conhecimento: "+areaExpertise);
	}
}	

//classe de turma
class Turma {
	//inicializa variáveis
	private String codigoDisciplina;
	private String nome;
	private int ano;
	private String area;
	private int limiteAlunos;
	private int horasAula;
	private List<Pessoa> participantes = new ArrayList<Pessoa>();
	private int nProfessores = 0;
	private int nAlunos = 0;

	//lista de turmas criadas
	static Hashtable<String, Turma> lista = new Hashtable<String, Turma>();

	//constructor da classe
	Turma (String id, String n, String an, String a, String lim, String ha) {
		codigoDisciplina = id;
		nome = n;
		ano = Integer.parseInt(an);
		area = a;
		limiteAlunos = Integer.parseInt(lim);
		horasAula = Integer.parseInt(ha);
	}

	//getter para nome de uma turma
	String getNome(){
		return nome;
	}

	//getter para codigo de uma turma
	String getCodigo(){
		return codigoDisciplina;
	}

	//getter para horas-aula de uma turma
	int getHA(){
		return horasAula;
	}

	//getter para ano no qual se desenvolve uma turma
	int getAno(){
		return ano;
	}

	//adiciona aluno ou professor
	void addParticipante(Pessoa p){
		participantes.add(p);
		if(p.getClass() == Professor.class)
			nProfessores++;
		else  if(p.getClass() == Aluno.class)
			nAlunos++;
	}

	//mostra as infos da turma
	 void info() {		
	 	System.out.println("Name: "+nome+" - "+codigoDisciplina);
	 	System.out.println("Ano: "+ano);
	 	System.out.println("Área: "+area);
	 	System.out.println("Número de Professores: "+nProfessores);
	 	System.out.println("Número de Alunos: "+nAlunos);
	 	System.out.println("Limite de Alunos: "+limiteAlunos); 		
	 	System.out.println("HA: "+horasAula);
	 }

	 //mostra participantes
	 void listaParticipantes(){
	 	System.out.println(nome+" - "+codigoDisciplina);	
	 	System.out.println("\nProfessores");
	 	for (Pessoa k : participantes){
	 		if(k.getClass() == Professor.class)
	 			System.out.println("Matrícula: " + k.getMatricula() + "| Nome: " + k.getNome());
	 	}
	 	System.out.println("\nAlunos");
	 	for (Pessoa k : participantes){
	 		if(k.getClass() == Aluno.class)
	 			System.out.println("Matrícula: " + k.getMatricula() + "| Nome: " + k.getNome());
	 	}
	 }
	}
