package modelo;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import clasesHibernate.Departamentos;

public class Modelo {
	// conecta con el config del hibernate
	private static final Configuration cfg = new Configuration().configure();
	// creas la sesión
	private static final SessionFactory sf = cfg.buildSessionFactory();
	private static Session sesion;

	public static void main(String[] args) {
		// anadirEmpleado("Juan", "Perez", "García", "Enfermería");
		// anadirDepartamento("Enfermeria", "Huelva");
		borrarDepartamento(44);
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
