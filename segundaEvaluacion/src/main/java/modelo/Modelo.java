package modelo;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import clasesHibernate.Departamentos;
import clasesHibernate.Empleados;
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
//		ArrayList<Departamentos> listadpto = listarDpto("Sevilla");
//		for (Departamentos a : listadpto) {
//			System.out.println(a.toString());
//		}

		anadirEmpleado("Juaa1a23an", "Maauiel", "Jacinto", "Jacinto");

	}

	private static ArrayList<Departamentos> listarDpto() {
		String hql = "from Departamentos";
		sesion = sf.openSession();
		TypedQuery<Departamentos> tqDpto = sesion.createQuery(hql, Departamentos.class);
		ArrayList<Departamentos> arrayDpto = (ArrayList<Departamentos>) tqDpto.getResultList();
		sesion.close();
		return arrayDpto;

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

	private static void modificarDpto(int idDpto, String nombreNew, String localidadNew) {

		// String hql = "from Departamentos where loc='" + localidad + "'";
		sesion = sf.openSession();
		Transaction t = sesion.beginTransaction();

		Departamentos dpto = sesion.get(Departamentos.class, idDpto);
		if (nombreNew != null) {

			dpto.setDnombre(nombreNew);

		}
		if (localidadNew != null) {
			dpto.setLoc(localidadNew);
		}
		sesion.merge(dpto);
		t.commit();
		sesion.close();

	}

	/**
	 * Comprobar que no existe un empleado con mismo nombre y apellidos, si es asi
	 * no hace nada el metodo, si no tiene que comprobar si existe el departamento
	 * 
	 * 
	 * Si el departamento no existe, lo creamos Si el departamento existe y hay
	 * varios con el mismo nombre, los mostramos y pedimos al usuario que elija
	 * entre ellos y se guarda el empleado Usar metodos externos para pedir datos
	 * por ejemplo
	 * 
	 * 
	 * @param nombre
	 * @param ap1
	 * @param ap2
	 * @param dpto
	 */
	private static void anadirEmpleado(String nombre, String ap1, String ap2, String dpto) {
		if (!comprobarExistenciaEmpleado(nombre, ap1, ap2)) {
			Departamentos dptoObjeto = comprobarExistenciaDepartamento(dpto);
			sesion = sf.openSession();
			Transaction t = sesion.beginTransaction();
			Empleados empleadoAnadir = new Empleados(dptoObjeto, nombre, ap1, ap2);
			sesion.persist(empleadoAnadir);
			t.commit();
			if (comprobarExistenciaEmpleado(nombre, ap1, ap2)) {
				System.out.println("Se ha añadido correctamente el empleado");
			} else {
				System.out.println("Error añadiendo el empleado");
			}
			sesion.close();

		} else {
			System.out.println("El empleado ya existe, por eso no se hizo nada");
		}
	}

	private static boolean comprobarExistenciaEmpleado(String nombre, String ap1, String ap2) {
		sesion = sf.openSession();
		String hql = "from Empleados where nombre='" + nombre + "' and apellido1='" + ap1 + "' and apellido2='" + ap2
				+ "'";
		TypedQuery tqDptotq = sesion.createQuery(hql, Departamentos.class);
		ArrayList<String> tqDpto = (ArrayList<String>) tqDptotq.getResultList();
		if (tqDpto.size() == 0) {
			sesion.close();
			return false;
		} else {
			sesion.close();
			return true;
		}

	}

	private static Departamentos comprobarExistenciaDepartamento(String dpto) {
		Scanner sc = new Scanner(System.in);
		sesion = sf.openSession();
		String elegirLocalidad = "Que localidad quieres elegir\n";
		String hql = "from Departamentos where dnombre='" + dpto + "'";
		TypedQuery tqDptoComprExis = sesion.createQuery(hql, Departamentos.class);
		ArrayList<Departamentos> tqDpto = (ArrayList<Departamentos>) tqDptoComprExis.getResultList();
		ArrayList<Departamentos> localidadesDptos = new ArrayList<Departamentos>();
		for (Departamentos d : tqDpto) {
			if (d.getDnombre().equalsIgnoreCase(dpto)) {
				elegirLocalidad += d.getLoc() + "\n";
				localidadesDptos.add(d);
			}

		}

		if (localidadesDptos.size() > 1) {
			System.out.println(elegirLocalidad);
			String localidad = sc.next();
			for (Departamentos d : localidadesDptos) {
				if (d.getLoc().equalsIgnoreCase(localidad)) {
					return d;
				}

			}

		} else if (localidadesDptos.size() == 1) {
			for (Departamentos d : localidadesDptos) {
				return d;
			}
		} else {
			System.out.println("Elige a que localidad pertenece el departamento");
			String localidad = sc.next();
			anadirDepartamento(dpto, localidad);

		}
		return comprobarExistenciaDepartamento(dpto);

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
