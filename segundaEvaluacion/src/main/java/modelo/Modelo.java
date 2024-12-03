package modelo;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import clasesHibernate.Departamentos;
import jakarta.persistence.TypedQuery;

public class Modelo {
	// conecta con el config del hibernate
	private static final Configuration cfg = new Configuration().configure();
	// creas la sesión
	private static final SessionFactory sf = cfg.buildSessionFactory();
	private static Session sesion;

	public static void main(String[] args) {
		// anadirEmpleado("Juan", "Perez", "García", "Enfermería");
		// anadirDepartamento("Enfermeria", "Huelva");
		// borrarDepartamento(44);
		ArrayList<Departamentos> listadpto = listarDpto("Sevilla");
		for (Departamentos a : listadpto) {
			System.out.println(a.toString());
		}

	}

	private static ArrayList<Departamentos> listarDpto() {
		String hql = "from Departamentos";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Departamentos> listarDpto(int dptoNum) {
		String hql = "from Departamentos where deptNo=" + dptoNum;
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Departamentos> listarDpto(String localidad) {
		String hql = "from Departamentos where loc='" + localidad + "'";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Departamentos> listarDptoNombre(String nombre) {
		String hql = "from Departamentos where dnombre='" + nombre + "'";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static ArrayList<Departamentos> modificarDpto(String localidad) {
		String hql = "from Departamentos where loc='" + localidad + "'";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		return (ArrayList<Departamentos>) tqDpto.getResultList();

	}

	private static void anadirEmpleado(String nombre, String ap1, String ap2, String dpto) {
		// TODO Auto-generated method stub

	}

	private static void anadirDepartamento(String nombreDepartamento, String localidad) {
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();
		Departamentos dpto = new Departamentos(nombreDepartamento, localidad, null);
		sesion.persist(dpto);
		t.commit();
		sesion.close();
	}

	private static boolean borrarDepartamento(int idDpto) {
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();
		Departamentos dptoBorrar = sesion.get(Departamentos.class, idDpto);
		if (JOptionPane.showConfirmDialog(null,
				"Quieres eliminar el departamento " + dptoBorrar.getDnombre() + "?") == JOptionPane.YES_OPTION) {
			sesion.remove(dptoBorrar);
			t.commit();
			sesion.close();
			return true;
		}

		t.rollback();
		sesion.close();
		return false;
	}

}
