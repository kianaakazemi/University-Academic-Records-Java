import java.util.*;

public class Main {
    public static int p;
    public static int a;
    public static int b;
    public static long hashing(long k){
        return (((a * k) + b) % p);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int T = scanner.nextInt();
        a = scanner.nextInt();
        b = scanner.nextInt();
        p = scanner.nextInt();
        Uni uni = new Uni();
        scanner.nextLine();

        for (int i = 0; i < T; i++) {
            String[] str = scanner.nextLine().split(" ");
            switch (str[0]){
                case "ADDS":
                    uni.adds.ADDS(str[1],str[2]);
                    break;
                case "ADDC":
                    uni.adds.ADDC(str[1],str[2]);
                    break;
                case "ADDG":
                    uni.adds.ADDG(str[1],str[2],str[3], str[4]);
                    break;
                case "EDITS":
                    uni.edits.EDITS(str[1],str[2]);
                    break;
                case "EDITC":
                    uni.edits.EDITC(str[1],str[2]);
                    break;
                case "EDITG":
                    uni.edits.EDITG(str[1],str[2], str[3]);
                    break;
                case "DELETES":
                    uni.deletes.DELETES(str[1]);
                    break;
                case "DELETEC":
                    uni.deletes.DELETEC(str[1]);
                    break;
                case "DELETEG":
                    uni.deletes.DELETEG(str[1],str[2]);
                    break;
                case "NUMBERS":
                    System.out.println(uni.numbers.NUMBERS(str[1]));
                    break;
                case "NUMBERC":
                    System.out.println(uni.numbers.NUMBERC(str[1]));
                    break;
                case "SEARCHSN":
                    System.out.println(uni.search.SEARCHSN(str[1]));
                    break;
                case "SEARCHCN":
                    System.out.println(uni.search.SEARCHCN(str[1]));
                    break;
                case "SEARCHSC":
                    String out = hashing(Long.parseLong(str[1]))% uni.getStudentsTable().getSize() + "\n" + uni.search.SEARCHSC(str[1]);
                    System.out.println(out);
                    break;
                case "SEARCHCC":
                    String out1 = hashing(Long.parseLong(str[1]))% uni.getCoursesTable().getSize() + "\n" + uni.search.SEARCHCC(str[1]);
                    System.out.println(out1);
                    break;
                case "ISRELATIVE":
                    if (!uni.isCanMakeCourseGraph()){
                        uni.setCanMakeCourseGraph(true);
                        uni.makeTheCoursesGraph();
                        uni.makeTheStudentsGraph();
                    }

                    if (uni.relatively.ISRELATIVE(str[1],str[2])) {

                        System.out.println("yes");
                    }else{
                        System.out.println("no");
                    }
                    uni.getCoursesGraph().setAllRelativeList(new LinkedList<>());

                    break;
                case "ALLRELATIVE":
                    if (!uni.isCanMakeCourseGraph()){
                        uni.setCanMakeCourseGraph(true);
                        uni.makeTheCoursesGraph();
                        uni.makeTheStudentsGraph();
                    }
                    System.out.println(uni.relatively.ALLRELATIVE(str[1]));
                    break;
                case "COMPARE":
                    if (!uni.isCanMakeCourseGraph()){
                        uni.setCanMakeCourseGraph(true);
                        uni.makeTheCoursesGraph();
                        uni.makeTheStudentsGraph();
                    }
                    uni.compare.COMPARE(str[1],str[2]);
                    break;
            }

        }
    }


}

class Adds {
    private final Uni uni;

    public Adds(Uni uni) {
        this.uni = uni;
    }

    public void ADDS(String code, String name){
        Student s = new Student(code,name);
        uni.getStudents().insert(s);
        if(Main.p > 1) {
            uni.getStudentsTree().insertStudent(uni.getStudentsTree().getRoot(), s);
            uni.getStudentsTable().insertStudent(s);
        }
    }

    public void ADDC(String code,String name){
        Course c = new Course(code,name);
        uni.getCourses().insert(c);

        if(Main.p > 1) {
            uni.getCoursesTree().insertCourse(uni.getCoursesTree().getRoot(), c);
            uni.getCoursesTable().insertCourse(c);
        }
    }

    public void ADDG(String studentCode, String courseCode, String semesterCode, String grade) {
        Grade g = new Grade(semesterCode,grade,studentCode,courseCode);
        addToCoursesGrades(g);
        addToStudentsGrades(g);
    }

    public void addToCoursesGrades(Grade grade){
        Node node = uni.findCourse(grade.getCourseCode());
        ((Course)node.getData()).getGrades().insert(grade);
    }

    public void addToStudentsGrades(Grade grade){
        Node node = uni.findStudent(grade.getStuCode());
        ((Student)node.getData()).getGrades().insert(grade);
    }
}


class BST {

    private BSTNode root;
    class BSTNode<E> {
        private E key;
        private BSTNode left;
        private BSTNode right;
        public BSTNode(E key) {
            this.key = key;
            left = right = null;
        }

        @Override
        public String toString() {
            return "BSTNode{" +
                    "key=" + key +
                    ", left=" + left +
                    ", right=" + right +
                    '}' + "\n";
        }

        public E getKey() {
            return key;
        }


        public BSTNode getLeft() {
            return left;
        }


        public BSTNode getRight() {
            return right;
        }

    }

    public BSTNode getRoot() {
        return root;
    }

    public BSTNode insertStudent(BSTNode root, Student key) {

        if (root == null) {
            root = new BSTNode(key);
            this.root = root;
            return root;
        }
        if ( key.getName().compareTo(((Student)root.key).getName()) < 0) {
            root.left = insertStudent(root.left, key);
        } else if (key.getName().compareTo(((Student)root.key).getName()) > 0) {
            root.right = insertStudent(root.right, key);
        }
        this.root = root;
        return root;
    }

    public BSTNode insertCourse(BSTNode root, Course key) {
        if (root == null) {
            root = new BSTNode(key);
            this.root = root;
            return root;
        } else if ( key.getName().compareTo(((Course)root.key).getName()) < 0) {
            root.left = insertCourse(root.left, key);
        } else if (key.getName().compareTo(((Course)root.key).getName()) > 0) {
            root.right = insertCourse(root.right, key);
        }
        this.root = root;
        return root;
    }

    public BSTNode searchStudent(BSTNode root, String key) {
        if (root==null || ((Student)root.key).getName().equals(key)) {
            return root;
        }

        if (key.compareTo(((Student)root.key).getName()) > 0) {
            return searchStudent(root.right, key);
        }
        return searchStudent(root.left, key);
    }


    public BSTNode searchCourse(BSTNode root, String key) {
        if (root==null || ((Course)root.key).getName().equals(key)) {
            return root;
        }

        if (key.compareTo(((Course)root.key).getName()) > 0) {
            return searchCourse(root.right, key);
        }
        return searchCourse(root.left, key);
    }



    public BSTNode minimum(BSTNode root){
        if(root.left == null){
            return root;
        }
        return minimum(root.left);
    }


    public BSTNode deleteStudent(BSTNode root, String key) {
        if (root == null) {
            this.root = null;
            return null;
        }
        if (key.compareTo(((Student) root.key).getName()) < 0) {
            root.left = deleteStudent(root.left, key);
        } else if (key.compareTo(((Student) root.key).getName()) > 0) {
            root.right = deleteStudent(root.right, key);

        } else {

            if (root.left != null && root.right != null) {
                BSTNode temp = root;
                BSTNode minNodeForRight = minimum(temp.right);
                root.key = minNodeForRight.key;
                root.right = deleteStudent(root.right, ((Student) minNodeForRight.key).getName());

            } else if (root.left != null) {
                root = root.left;
            } else if (root.right != null) {
                root = root.right;
            } else
                root = null;
        }
        this.root = root;
        return root;
    }


    public BSTNode deleteCourse(BSTNode root, String key) {
        if (root == null)
            return null;
        if (key.compareTo(((Course) root.key).getName()) < 0) {
            root.left = deleteCourse(root.left, key);
        } else if (key.compareTo(((Course) root.key).getName()) > 0) {
            root.right = deleteCourse(root.right, key);

        } else {
            if (root.left != null && root.right != null) {
                BSTNode temp = root;
                BSTNode minNodeForRight = minimum(temp.right);
                root.key = minNodeForRight.key;
                root.right = deleteCourse(root.right, ((Course) minNodeForRight.key).getName());

            } else if (root.left != null) {
                root = root.left;
            } else if (root.right != null) {
                root = root.right;
            } else
                root = null;
        }
        return root;
    }

    @Override
    public String toString() {
        return "BST{" +
                "root=" + root +
                '}';
    }
}

class Course {
    private String code;
    private String name;
    private final LinkedList<Grade> grades;

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
        grades = new LinkedList<>();
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LinkedList<Grade> getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", grades=" + grades +
                '}';
    }
}


class Deletes {
    private final Uni uni;

    public Deletes(Uni uni) {
        this.uni = uni;
    }

    public void DELETES(String code){
        DELETESFromCGL(code);
        Student s = DELETESFromSL(code);
        if(Main.p > 1) {
            DELETESFROMST(s);
            DELETESFromSTa(code);
        }
    }

    public Student DELETESFromSL(String code){
        Node current = uni.getStudents().getHead();
        Node previous = null;

        while (current != null && !((Student)current.getData()).getCode().equals(code)){
            previous = current;
            current = current.getNext();
        }

        if (current != null) {
            if(previous == null){
                uni.getStudents().setHead(current.getNext());
            }else {
                if (current == uni.getStudents().getTail()) {
                    uni.getStudents().setTail(previous);
                }
                previous.setNext(current.getNext());
            }
        }
        uni.getStudents().setSize(uni.getStudents().getSize() - 1);
        assert current != null;
        return (Student)current.getData();
    }


    public void DELETESFromCGL(String studentCode){
        Node currentCourse = uni.getCourses().getHead();
        for (int i = 0; i < uni.getCourses().getSize(); i++) {
            LinkedList<Grade> grades  = ((Course)currentCourse.getData()).getGrades();
            deleteGradeFromEachCourse(grades,studentCode);
            currentCourse = currentCourse.getNext();
        }
    }

    public void DELETESFROMST(Student student){
        uni.getStudentsTree().deleteStudent(uni.getStudentsTree().getRoot(),student.getName());
    }

    public void deleteGradeFromEachCourse(LinkedList<Grade> grades,String studentCode){
        Node currentGrade = grades.getHead();
        Node previous = null;
        while (currentGrade != null && !((Grade)currentGrade.getData()).getStuCode().equals(studentCode)){
            previous = currentGrade;
            currentGrade = currentGrade.getNext();
        }
        if (currentGrade != null) {
            if(previous == null){
                grades.setHead(currentGrade.getNext());
            }else {
                if (currentGrade == grades.getTail()) {
                    grades.setTail(previous);
                }
                previous.setNext(currentGrade.getNext());
            }
            grades.setSize(grades.getSize() - 1);
        }
    }

    public void DELETESFromSTa(String code){
        uni.getStudentsTable().deleteStudent(code);
    }


    public void DELETEC(String code){
        DELETECFromSGL(code);
        Course c = DELETECFromCL(code);
        if(Main.p > 1) {
            DELETECFROMCT(c);
            DELETECFromCTa(code);
        }
    }

    public Course DELETECFromCL(String code){
        Node current = uni.getCourses().getHead();
        Node previous = null;

        while (current != null && !((Course)current.getData()).getCode().equals(code)){
            previous = current;
            current = current.getNext();
        }

        if (current != null) {
            if(previous == null){
                uni.getCourses().setHead(current.getNext());
            }else {
                if (current == uni.getCourses().getTail()) {
                    uni.getCourses().setTail(previous);
                }
                previous.setNext(current.getNext());
            }
        }
        uni.getCourses().setSize(uni.getCourses().getSize() - 1);
        assert current != null;
        return (Course)current.getData();
    }

    public void DELETECFromSGL(String courseCode){
        Node currentCourse = uni.getStudents().getHead();
        for (int i = 0; i < uni.getStudents().getSize(); i++) {
            LinkedList<Grade> grades  = ((Student)currentCourse.getData()).getGrades();
            deleteGradeFromEachStudent(grades,courseCode);
            currentCourse = currentCourse.getNext();
        }
    }


    public void DELETECFROMCT(Course course){
        uni.getCoursesTree().deleteCourse(uni.getCoursesTree().getRoot(), course.getName());
    }


    public void deleteGradeFromEachStudent(LinkedList<Grade> grades,String courseCode){
        Node currentGrade = grades.getHead();
        Node previous = null;
        while (currentGrade != null && !((Grade)currentGrade.getData()).getCourseCode().equals(courseCode)){
            previous = currentGrade;
            currentGrade = currentGrade.getNext();
        }
        if (currentGrade != null) {
            if(previous == null){
                grades.setHead(currentGrade.getNext());
            }else {
                if (currentGrade == grades.getTail()) {
                    grades.setTail(previous);
                }
                previous.setNext(currentGrade.getNext());
            }
            grades.setSize(grades.getSize() - 1);
        }
    }

    public void DELETECFromCTa(String code){
        uni.getCoursesTable().deleteCourse(code);
    }



    public void DELETEG(String studentCode,String courseCode){
        deleteFromCoursesList(studentCode, courseCode);
        deleteFromStudentsList(studentCode, courseCode);
    }

    public void deleteGradeFromAStudentList(LinkedList<Grade> grades,String courseCode){
        Node current = grades.getHead();
        Node previous = null;

        while (current != null && !((Grade)current.getData()).getCourseCode().equals(courseCode)){
            previous = current;
            current = current.getNext();
        }
        if (current != null) {
            if(previous == null){
                grades.setHead(current.getNext());
            }else {
                if (current == grades.getTail()) {
                    grades.setTail(previous);
                }
                previous.setNext(current.getNext());
            }
        }
        grades.setSize(grades.getSize() - 1);
    }

    public void deleteGradeFromACourseList(LinkedList<Grade> grades,String studentCode){
        Node current = grades.getHead();
        Node previous = null;

        while (current != null && !((Grade)current.getData()).getStuCode().equals(studentCode)){
            previous = current;
            current = current.getNext();
        }
        if (current != null) {
            if(previous == null){
                grades.setHead(current.getNext());
            }else {
                if (current == grades.getTail()) {
                    grades.setTail(previous);
                }
                previous.setNext(current.getNext());
            }
        }
        grades.setSize(grades.getSize() - 1);
    }

    public void deleteFromStudentsList(String studentCode,String courseCode){
        Node node = uni.findStudent(studentCode);
        LinkedList<Grade> grades = ((Student)node.getData()).getGrades();
        deleteGradeFromAStudentList(grades,courseCode);
    }

    public void deleteFromCoursesList(String studentCode,String courseCode){
        Node node = uni.findCourse(courseCode);
        LinkedList<Grade> grades = ((Course)node.getData()).getGrades();
        deleteGradeFromACourseList(grades,studentCode);
    }

}


class Edits {
    private final Uni uni;

    public Edits(Uni uni) {
        this.uni = uni;
    }

    public void EDITS(String code, String newName){
        editSInListAndTree(code, newName);
    }

    public void editSInListAndTree(String code, String newName){
        Node current = uni.findStudent(code);
        if (current != null) {
            Student student = ((Student)current.getData());
            String name = student.getName();
            if(Main.p > 1) {
                uni.getStudentsTree().deleteStudent(uni.getStudentsTree().getRoot(),name);
                student.setName(newName);
                uni.getStudentsTree().insertStudent(uni.getStudentsTree().getRoot(),student);
            }else {
                student.setName(newName);
            }
        }
    }

    public void EDITC(String code,String newName){
        editCInListAndTree(code, newName);
    }

    public void editCInListAndTree(String code, String newName){
        Node current = uni.findCourse(code);
        if (current != null) {
            Course course = ((Course)current.getData());
            String name = course.getName();
            if(Main.p > 1) {
                uni.getCoursesTree().deleteCourse(uni.getCoursesTree().getRoot(),name);
                course.setName(newName);
                uni.getCoursesTree().insertCourse(uni.getCoursesTree().getRoot(),course);
            }else {
                course.setName(newName);
            }
        }
    }

    public void EDITG(String studentCode, String courseCode, String newGrade){
        editGInStudentsGrades(studentCode,courseCode,newGrade);
        editGInCoursesGrades(studentCode,courseCode,newGrade);
    }

    public void editGInStudentsGrades(String studentCode, String courseCode, String newGrade){
        Node studentNode = uni.findStudent(studentCode);
        Node current = ((Student)studentNode.getData()).getGrades().getHead();
        while (current != null && !((Grade)current.getData()).getCourseCode().equals(courseCode)){
            current = current.getNext();
        }

        if (current != null) {
            ((Grade)current.getData()).setGrade(newGrade);
        }
    }

    public void editGInCoursesGrades(String studentCode, String courseCode, String newGrade){
        Node courseNode = uni.findCourse(courseCode);
        Node current = ((Course)courseNode.getData()).getGrades().getHead();
        while (current != null && !((Grade)current.getData()).getStuCode().equals(studentCode)){
            current = current.getNext();
        }

        if (current != null) {
            ((Grade)current.getData()).setGrade(newGrade);
        }
    }

}


class Grade {
    private final String semester;
    private String grade;

    private final String stuCode;
    private final String courseCode;

    public Grade(String semester, String grade, String stuCode, String courseCode) {
        this.semester = semester;
        this.grade = grade;
        this.stuCode = stuCode;
        this.courseCode = courseCode;
    }

    public String getSemester() {
        return semester;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getStuCode() {
        return stuCode;
    }
    public String getCourseCode() {
        return courseCode;
    }


    @Override
    public String toString() {
        return "Grade{" +
                "semester='" + semester + '\'' +
                ", grade='" + grade + '\'' +
                ", stuNum='" + stuCode + '\'' +
                ", courseNum='" + courseCode + '\'' +
                '}';
    }
}


class LinkedList <E>{
    private Node head;
    private Node tail;
    private int size = 0;
    public void insert(E value){
        Node newNode = new Node<>(value);

        if (getHead() == null) {
            setHead(newNode);
            setTail(newNode);
        }else if (head == tail) {
            tail = newNode;
            head.setNext(tail);

        }else{
            tail.setNext(newNode);
            tail = newNode;
            newNode.setNext(null);
        }
        size++;
    }

    public boolean contains(E data){
        Node current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    public Node getHead() {
        return head;
    }
    public void setHead(Node head) {
        this.head = head;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    @Override
    public String toString() {
        String str = "";
        Node current = head;
        for (int i = 0; i < size; i++) {
            str += "/" + current.toString();
            current = current.getNext();
        }
        return "LinkedList{" + str + '}';
    }
}



class Node<E>{
    private Node next;
    private E data;

    public Node(E data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }


    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}

class Numbers {

    private final Uni uni;

    public Numbers(Uni uni) {
        this.uni = uni;
    }

    public int NUMBERS(String code) {
        Node node = uni.findCourse(code);
        return ((Course) node.getData()).getGrades().getSize();
    }

    public int NUMBERC (String code){
        Node node = uni.findStudent(code);
        return ((Student)node.getData()).getGrades().getSize();
    }
}


class Search {
    private final Uni uni;

    public Search(Uni uni) {
        this.uni = uni;
    }

    public String SEARCHSN(String studentName){
        BST.BSTNode node = uni.getStudentsTree().searchStudent(uni.getStudentsTree().getRoot(),studentName);
        return buildStudentString((Student) node.getKey());

    }

    public String SEARCHCN(String courseName){
        BST.BSTNode node = uni.getCoursesTree().searchCourse(uni.getCoursesTree().getRoot(),courseName);
        return buildCourseString((Course)node.getKey());
    }

    public String SEARCHSC(String studentCode){
        Student s = uni.getStudentsTable().searchStudent(studentCode);
        return buildStudentString(s);
    }

    public String SEARCHCC(String courseCode){
        Course c = uni.getCoursesTable().searchCourse(courseCode);
        return buildCourseString(c);
    }

    public String buildStudentString(Student student){
        String out = student.getCode() + " " + student.getName() + " " + student.getGrades().getSize();
        Node current = student.getGrades().getHead();
        for (int i = 0; i < student.getGrades().getSize(); i++) {
            Grade g = (Grade)current.getData();
            out += "\n" + g.getCourseCode() + " " + g.getSemester() + " " + g.getGrade();
            current = current.getNext();
        }
        return out;
    }

    public String buildCourseString(Course course){
        String out = course.getCode() + " " + course.getName() + " " + course.getGrades().getSize();
        Node current = course.getGrades().getHead();
        for (int i = 0; i < course.getGrades().getSize(); i++) {
            Grade g = (Grade)current.getData();
            out += "\n" + g.getStuCode() + " " + g.getSemester() + " " + g.getGrade();
            current = current.getNext();
        }
        return out;
    }
}


class Student {
    private final String code;
    private String name;
    private final LinkedList<Grade> grades;

    public Student(String code, String name) {
        this.code = code;
        this.name = name;
        grades = new LinkedList<>();
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LinkedList<Grade> getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        return "Student{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", grades=" + grades +
                '}';
    }
}

class Uni {

    private boolean canMakeCourseGraph = false;
    private static final LinkedList<Student> students = new LinkedList<>();
    private static final LinkedList<Course> courses = new LinkedList<>();

    private final BST studentsTree = new BST();
    private final BST coursesTree = new BST();

    private final HashTable studentsTable = new HashTable();
    private final HashTable coursesTable = new HashTable();
    private final Graph<Course> coursesGraph = new Graph<>();
    private final Graph<Student> studentsGraph = new Graph<>();


    public Edits edits;
    public Adds adds;
    public Deletes deletes;

    public Numbers numbers;

    public Search search;
    public Relatively relatively;
    public Compare compare;

    public Uni() {
        adds = new Adds(this);
        edits = new Edits(this);
        deletes = new Deletes(this);
        numbers = new Numbers(this);
        search = new Search(this);
        relatively = new Relatively(this);
        compare = new Compare(this);
    }



    public LinkedList<Student> getStudents() {
        return students;
    }

    public LinkedList<Course> getCourses() {
        return courses;
    }

    public BST getStudentsTree() {
        return studentsTree;
    }

    public BST getCoursesTree() {
        return coursesTree;
    }

    public boolean isCanMakeCourseGraph() {
        return canMakeCourseGraph;
    }

    public Graph<Course> getCoursesGraph() {
        return coursesGraph;
    }

    public HashTable getStudentsTable() {
        return studentsTable;
    }

    public HashTable getCoursesTable() {
        return coursesTable;
    }

    public void setCanMakeCourseGraph(boolean canMakeCourseGraph) {
        this.canMakeCourseGraph = canMakeCourseGraph;
    }


    public Graph<Student> getStudentsGraph() {
        return studentsGraph;
    }

    public Node findCourse(String courseCode){
        Node current = courses.getHead();

        while (current != null && !((Course)current.getData()).getCode().equals(courseCode)){
            current = current.getNext();
        }
        return current;
    }

    public Node findStudent(String studentCode){
        Node current = students.getHead();
        while (current != null && !((Student)current.getData()).getCode().equals(studentCode)){
            current = current.getNext();
        }
        return current;
    }

    public void makeTheCoursesGraph(){
        Node node = courses.getHead();
        for (int i = 0; i < courses.getSize(); i++) {
            coursesGraph.addVertex((Course) node.getData());
            node = node.getNext();
        }

        Node n1 = courses.getHead();
        for (int i = 0; i < courses.getSize(); i++) {
            Course c1 = (Course) n1.getData();
            Node n2 = n1.getNext();
            for (int j = i+1; j < courses.getSize(); j++) {
                Course c2 = (Course) n2.getData();
                if(relatively.ISDirectRELATIVE(c1.getCode(),c2.getCode())){
                    coursesGraph.addEdge(coursesGraph.findCourseVertex(c1.getCode()),coursesGraph.findCourseVertex(c2.getCode()));
                }
                n2 = n2.getNext();
            }
            n1 = n1.getNext();
        }

    }

    public void makeTheStudentsGraph(){
        Node node = students.getHead();
        while ( node!= null) {
            studentsGraph.addVertex((Student) node.getData());
            node = node.getNext();
        }

        Node n1 = students.getHead();
        while (n1 != null){
            Node n2 = n1.getNext();
            while (n2 != null){
                mutualCourses((Student) n1.getData(), (Student) n2.getData());
                n2 = n2.getNext();
            }
            n1 = n1.getNext();
        }

    }

    public void mutualCourses(Student s1 ,Student s2){
        int mutual = 0;
        int bigger1 = 0;
        int bigger2 = 0;
        Node n1 = s1.getGrades().getHead();
        while (n1 != null){
            Node n2 = s2.getGrades().getHead();
            while (n2 != null){
                if(!(s1.getCode().equals(s2.getCode())) && ((Grade)n1.getData()).getCourseCode().equals(((Grade)n2.getData()).getCourseCode())){
                    mutual++;
                    if(Double.parseDouble(((Grade)n1.getData()).getGrade()) > Double.parseDouble(((Grade)n2.getData()).getGrade())){
                        bigger1++;
                    } else if(Double.parseDouble(((Grade)n1.getData()).getGrade()) < Double.parseDouble(((Grade)n2.getData()).getGrade())) {
                        bigger2++;
                    }
                }
                n2 = n2.getNext();
            }
            n1 = n1.getNext();
        }

        if(bigger1 > mutual / 2){
            studentsGraph.addDirectEdge(studentsGraph.findStudentVertex(s1.getCode()),studentsGraph.findStudentVertex(s2.getCode()));
        } else if(bigger2 > mutual / 2){
            studentsGraph.addDirectEdge(studentsGraph.findStudentVertex(s2.getCode()),studentsGraph.findStudentVertex(s1.getCode()));
        }

    }

}

class HashTable {
    private LinkedList[] table;
    private LinkedList[] old = new LinkedList[0];
    private long size = 1;
    private long n = 0;
    private final long a;
    private final long b;
    private final long p;



    public HashTable(){
        table = new LinkedList[(int) size];
        for (int i = 0; i < size; i++) {
            table[i] = new LinkedList<>();
        }
        a = Main.a;
        b = Main.b;
        p = Main.p;
    }

    public LinkedList[] getTable() {
        return table;
    }

    public long getSize() {
        return size;
    }

    public long hashing(long k){
        return (((a * k) + b) % p) % size;
    }

    public void insertStudent(Student student){
        n++;
        if (size == n) {
            extendStudentTable();
        }
        if (table[(int) hashing(Long.parseLong(student.getCode()))] == null) {
            table[(int) hashing( Long.parseLong(student.getCode()))] = new LinkedList<>();
        }
        table[(int) hashing( Long.parseLong(student.getCode()))].insert(student);
    }

    public void insertCourse(Course course){
        n++;
        if (size == n) {
            extendCourseTable();
        }
        if (table[(int) hashing(Long.parseLong(course.getCode()))] == null) {
            table[(int) hashing( Long.parseLong(course.getCode()))] = new LinkedList<>();
        }
        table[(int) hashing(Integer.parseInt(course.getCode()))].insert(course);
    }

    public void extendStudentTable(){
        old = table;
        table = new LinkedList[(int) (size*2)];
        size = size*2;
        for (LinkedList linkedList : old) {
            Node node = null;
            if (linkedList != null) {
                node = linkedList.getHead();
            }
            while (node != null) {
                Student s = (Student) node.getData();
                if (table[(int) hashing(Long.parseLong(s.getCode()))] == null) {
                    table[(int) hashing(Long.parseLong(s.getCode()))] = new LinkedList<>();
                }
                table[(int) hashing(Long.parseLong(s.getCode()))].insert(s);
                node = node.getNext();
            }
        }

    }

    public void extendCourseTable(){
        old = table;
        table = new LinkedList[(int) (size*2)];
        size = size*2;
        for (LinkedList linkedList : old) {
            Node node = null;
            if (linkedList != null) {
                node = linkedList.getHead();
            }
            while (node != null) {
                Course c = (Course) node.getData();
                if (table[(int) hashing(Long.parseLong(c.getCode()))] == null) {
                    table[(int) hashing(Long.parseLong(c.getCode()))] = new LinkedList<>();
                }
                table[(int) hashing(Long.parseLong(c.getCode()))].insert(c);
                node = node.getNext();
            }
        }

    }

    public void shrinkStudentTable(){
        old = table;
        table = new LinkedList[(int) (size/2)];
        size = size/2;
        for (LinkedList linkedList : old) {
            Node node = null;
            if (linkedList != null) {
                node = linkedList.getHead();
            }
            while (node != null) {
                Student s = (Student) node.getData();
                if (table[(int) hashing(Long.parseLong(s.getCode()))] == null) {
                    table[(int) hashing(Long.parseLong(s.getCode()))] = new LinkedList<>();
                }
                table[(int) hashing(Long.parseLong(s.getCode()))].insert(s);
                node = node.getNext();
            }
        }

    }

    public void shrinkCourseTable(){
        old = table;
        table = new LinkedList[(int) (size/2)];
        size = size/2;
        for (LinkedList linkedList : old) {
            Node node = null;
            if (linkedList != null) {
                node = linkedList.getHead();
            }
            while (node != null) {
                Course c = (Course) node.getData();
                if (table[(int) hashing(Long.parseLong(c.getCode()))] == null) {
                    table[(int) hashing(Long.parseLong(c.getCode()))] = new LinkedList<>();
                }
                table[(int) hashing(Long.parseLong(c.getCode()))].insert(c);
                node = node.getNext();
            }
        }

    }

    public Student searchStudent(String code){
        LinkedList students = table[(int) hashing(Long.parseLong(code))];
        Node current = students.getHead();
        for (int i = 0; i < students.getSize(); i++) {
            if (((Student)( current.getData())).getCode().equals(code)) {
                return (Student) current.getData();
            }
            current = current.getNext();
        }
        return null;
    }

    public Course searchCourse(String code){
        LinkedList courses = table[(int) hashing(Long.parseLong(code))];
        Node current = courses.getHead();
        for (int i = 0; i < courses.getSize(); i++) {
            if (((Course) ( current.getData())).getCode().equals(code)) {
                return (Course) current.getData();
            }
            current = current.getNext();
        }
        return null;
    }

    public void deleteStudent(String code){
        n--;
        LinkedList students = table[(int) hashing(Long.parseLong(code))];
        Node current = students.getHead();
        Node previous = null;

        while (current != null && !((Student)current.getData()).getCode().equals(code)){
            previous = current;
            current = current.getNext();
        }

        if (current != null) {
            if(previous == null){
                students.setHead(current.getNext());
            }else {
                previous.setNext(current.getNext());
            }
        }
        students.setSize(students.getSize() - 1);

        if (n < size/4) {
            shrinkStudentTable();
        }

    }

    public void deleteCourse(String code){
        n--;
        LinkedList courses = table[(int) hashing(Long.parseLong(code))];
        Node current = courses.getHead();
        Node previous = null;

        while (current != null && !((Course)current.getData()).getCode().equals(code)){
            previous = current;
            current = current.getNext();
        }

        if (current != null) {
            if(previous == null){
                courses.setHead(current.getNext());
            }else {
                previous.setNext(current.getNext());
            }
        }
        courses.setSize(courses.getSize() - 1);

        if (n < size/4) {
            shrinkCourseTable();
        }

    }

    @Override
    public String toString() {
        return "HashTable{" +
                "table=" + Arrays.toString(table) +
                ", size=" + size +
                '}';
    }
}

class Graph <E>{
    private final LinkedList<Vertex<E>> vertices;
    private LinkedList<Long> allRelativeList = new LinkedList<>();

    public Graph() {
        this.vertices = new LinkedList<>();
    }

    public void addVertex(E value) {
        vertices.insert(new Vertex<>(value));
    }

    public void addEdge(Vertex<E> origin, Vertex<E> destination) {
        if(origin.edges.contains(destination) || destination.edges.contains(origin)){
            return;
        }
        origin.addEdge(destination);
        destination.addEdge(origin);
    }

    void addDirectEdge(Vertex<E> origin, Vertex<E> destination) {
        if(origin.edges.contains(destination)) {
            return;
        }
        origin.addEdge(destination);
    }

    public Vertex findCourseVertex(String courseCode) {

        Node current = vertices.getHead();

        while (current != null && !((Vertex<Course>)current.getData()).value.getCode().equals(courseCode)){
            current = current.getNext();
        }
        if (current != null) {
            return  (Vertex<Course>)current.getData();
        }
        return null;
    }

    public Vertex findStudentVertex(String studentCode) {

        Node current = vertices.getHead();

        while (current != null && !((Vertex<Student>)current.getData()).value.getCode().equals(studentCode)){
            current = current.getNext();
        }
        if (current != null) {
            return  (Vertex<Student>)current.getData();
        }
        return null;
    }

    public boolean DFSForRelation(Vertex<Course> course1, Vertex<Course> course2, LinkedList<Vertex<Course>> visited) {
        Stack<Vertex<Course>> stack = new Stack<>();
        stack.push(course1);
        while (!stack.isEmpty()) {
            Vertex<Course> current = stack.pop();
            visited.insert(current);
            if (current.value.getCode().equals(course2.value.getCode())) {
                allRelativeList.insert(Long.parseLong(course2.value.getCode()));
                return true;
            }
            Node thisNode = current.edges.getHead();
            while (thisNode != null) {
                if (!visited.contains((Vertex<Course>) thisNode.getData())) {
                    stack.push((Vertex<Course>) thisNode.getData());
                }
                thisNode = thisNode.getNext();
            }
        }
        return false;
    }

    public boolean DFSForCompare(Vertex<Student> student1, Vertex<Student> student2 ) {
        LinkedList<Vertex<Student>> visited = new LinkedList<>();
        Stack<Vertex<Student>> stack = new Stack<>();
        stack.push(student1);
        visited.insert(student1);
        while (!stack.isEmpty()) {
            Vertex<Student> current = stack.pop();
            if (current.value.getCode().equals(student2.value.getCode())) {
                return true;
            }
            Node thisNode = current.edges.getHead();
            while (thisNode != null) {
                if (!visited.contains((Vertex<Student>) thisNode.getData())) {
                    stack.push((Vertex<Student>) thisNode.getData());
                    visited.insert((Vertex<Student>) thisNode.getData());
                }
                thisNode = thisNode.getNext();
            }
        }
        return false;
    }

    public LinkedList<Long> getAllRelativeList() {
        return allRelativeList;
    }

    public void setAllRelativeList(LinkedList<Long> allRelativeList) {
        this.allRelativeList = allRelativeList;
    }




    public static class Vertex<E> {
        private final E value;
        private final LinkedList<Vertex<E>> edges;

        public Vertex(E value) {
            this.value = value;
            this.edges = new LinkedList<>();
        }
        public E getValue() {
            return value;
        }

        public void addEdge(Vertex<E> vertex) {
            edges.insert(vertex);
        }

        public LinkedList<Vertex<E>> getEdges() {
            return edges;
        }
    }


}

class Relatively {
    private final Uni uni;

    public Relatively(Uni uni) {
        this.uni = uni;
    }


    public boolean ISDirectRELATIVE(String course1, String course2){
        Course c1 = (Course) uni.findCourse(course1).getData();
        Course c2 = (Course) uni.findCourse(course2).getData();
        Node node1 = c1.getGrades().getHead();
        int mutual = 0;
        for (int i = 0; i < c1.getGrades().getSize(); i++) {
            Node node2 = c2.getGrades().getHead();
            for (int j = 0; j < c2.getGrades().getSize(); j++) {
                if (((Grade) node1.getData()).getStuCode().equals(((Grade) node2.getData()).getStuCode())) {
                    mutual++;
                }
                node2 = node2.getNext();
            }
            node1 = node1.getNext();
        }
        return mutual > c1.getGrades().getSize()/2 && mutual > c2.getGrades().getSize()/2;
    }






    public boolean ISRELATIVE(String course1, String course2){
        Graph.Vertex<Course> c1 = uni.getCoursesGraph().findCourseVertex(course1);
        Graph.Vertex<Course> c2 = uni.getCoursesGraph().findCourseVertex(course2);
        return uni.getCoursesGraph().DFSForRelation(c1, c2, new LinkedList<>());
    }

    public String ALLRELATIVE(String courseCode){
        Course course = (Course) uni.findCourse(courseCode).getData();
        Node current = uni.getCourses().getHead();
        while (current != null){
            if (!((Course) current.getData()).getCode().equals(courseCode)) {
                ISRELATIVE(course.getCode(),((Course) current.getData()).getCode());
            }
            current = current.getNext();
        }
        quickSort(uni.getCoursesGraph().getAllRelativeList().getHead(),uni.getCoursesGraph().getAllRelativeList().getTail());
        String s = output();
        uni.getCoursesGraph().setAllRelativeList(new LinkedList<>());
        return s;
    }


    public Node partition(Node start, Node end)
    {
        if (start == end || start == null || end == null) {
            return start;
        }

        Node pivotPrev = start;
        Node current = start;
        long pivot = (Long)end.getData();

        while (start != end) {
            if ((Long)start.getData() < pivot) {

                pivotPrev = current;
                long temp = (Long)current.getData();
                current.setData(start.getData());
                start.setData(temp);
                current = current.getNext();
            }
            start = start.getNext();
        }


        long temp = (Long)current.getData();
        current.setData(pivot);
        end.setData(temp);

        return pivotPrev;
    }

    public void quickSort(Node start, Node end)
    {
        if (start == null || start == end || start == end.getNext()) {
            return;
        }

        Node q = partition(start, end);
        quickSort(start, q);

        if (q != null && q == start) {
            quickSort(q.getNext(), end);
        } else if (q != null && q.getNext() != null) {
            quickSort(q.getNext().getNext(), end);
        }
    }


    public String output(){
        Node current = uni.getCoursesGraph().getAllRelativeList().getHead();
        String out = "";
        while (current != null) {
            out += current.getData() + " ";
            current = current.getNext();
        }
        return out;
    }
}

class Stack<E> {
    private stackNode top;
    class stackNode<E>{
        private E data;
        private stackNode link;

        public void setData(E data) {
            this.data = data;
        }

        public void setLink(stackNode link) {
            this.link = link;
        }

        public E getData() {
            return data;
        }

        public stackNode getLink() {
            return link;
        }
    }

    public Stack() {
        top = null;
    }

    public void push(E data){
        stackNode temp = new stackNode<>();
        temp.data = data;
        temp.link = top;
        top = temp;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public E peek() {
        if (!isEmpty()) {
            return (E) top.data;
        }
        return null;
    }

    public E pop(){
        if (top == null) {
            return null;
        }
        Stack.stackNode temp = top;
        top = top.link;
        return (E)temp.data;
    }
}

class Compare {
    private final Uni uni;

    public Compare(Uni uni) {
        this.uni = uni;
    }

    public void COMPARE(String student1, String student2) {
        Graph.Vertex<Student> s1 = uni.getStudentsGraph().findStudentVertex(student1);
        Graph.Vertex<Student> s2 = uni.getStudentsGraph().findStudentVertex(student2);
        boolean b1 = uni.getCoursesGraph().DFSForCompare(s1, s2);
        boolean b2 = uni.getCoursesGraph().DFSForCompare(s2, s1);
        if (b1 && b2) {
            System.out.println("?");
        } else if (b1) {
            System.out.println(">");
        } else if (b2) {
            System.out.println("<");
        } else {
            System.out.println("?");
        }

    }
}