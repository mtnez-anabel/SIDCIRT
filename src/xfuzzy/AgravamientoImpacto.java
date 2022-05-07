package xfuzzy;

//++++++++++++++++++++++++++++++++++++++++++++++++++++++//
//     Fuzzy Inference Engine AgravamientoImpacto      //
//++++++++++++++++++++++++++++++++++++++++++++++++++++++//

public class AgravamientoImpacto implements FuzzyInferenceEngine {

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function of an input variable       //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private abstract class InnerMembershipFunction {
  double min, max, step;
  abstract double param(int i);
  double center() { return 0; }
  double basis() { return 0; }
  abstract double isEqual(double x);

  double isSmallerOrEqual(double x) {
   double degree=0, mu;
   for(double y=max; y>=x ; y-=step) if((mu = isEqual(y))>degree) degree=mu;
   return degree;
  }

  double isGreaterOrEqual(double x) {
   double degree=0, mu;
   for(double y=min; y<=x ; y+=step) if((mu = isEqual(y))>degree) degree=mu;
   return degree;
  }

  double isEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isGreaterOrEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isGreaterOrEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isGreaterOrEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0,greq=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>greq ) greq = mu2;
    if( mu1<greq ) minmu = mu1; else minmu = greq;
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSmallerOrEqual(MembershipFunction mf) {
   if(mf instanceof FuzzySingleton)
    { return isSmallerOrEqual( ((FuzzySingleton) mf).getValue()); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = isSmallerOrEqual(val[i][0]);
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0,smeq=0;
   for(double x=max; x>=min; x-=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>smeq ) smeq = mu2;
    if( mu1<smeq ) minmu = mu1; else minmu = smeq;
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isGreater(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isSmallerOrEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isSmallerOrEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,gr,degree=0,smeq=0;
   for(double x=max; x>=min; x-=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>smeq ) smeq = mu2;
    gr = op.not(smeq);
    minmu = ( mu1<gr ? mu1 : gr);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSmaller(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isGreaterOrEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isGreaterOrEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,sm,degree=0,greq=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = isEqual(x);
    if( mu2>greq ) greq = mu2;
    sm = op.not(greq);
    minmu = ( mu1<sm ? mu1 : sm);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isNotEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.not(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.not(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.not(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isApproxEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.moreorless(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.moreorless(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.moreorless(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isVeryEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.very(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.very(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.very(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }

  double isSlightlyEqual(MembershipFunction mf, InnerOperatorset op) {
   if(mf instanceof FuzzySingleton)
    { return op.slightly(isEqual( ((FuzzySingleton) mf).getValue())); }
   if((mf instanceof OutputMembershipFunction) &&
      ((OutputMembershipFunction) mf).isDiscrete() ) {
    double[][] val = ((OutputMembershipFunction) mf).getDiscreteValues();
    double deg = 0;
    for(int i=0; i<val.length; i++){
     double mu = op.slightly(isEqual(val[i][0]));
     double minmu = (mu<val[i][1] ? mu : val[i][1]);
     if( deg<minmu ) deg = minmu;
    }
    return deg;
   }
   double mu1,mu2,minmu,degree=0;
   for(double x=min; x<=max; x+=step){
    mu1 = mf.compute(x);
    mu2 = op.slightly(isEqual(x));
    minmu = (mu1<mu2 ? mu1 : mu2);
    if( degree<minmu ) degree = minmu;
   }
   return degree;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //          Abstract class of an operator set          //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private abstract class InnerOperatorset {
  abstract double and(double a, double b);
  abstract double or(double a, double b);
  abstract double also(double a, double b);
  abstract double imp(double a, double b);
  abstract double not(double a);
  abstract double very(double a);
  abstract double moreorless(double a);
  abstract double slightly(double a);
  abstract double defuz(OutputMembershipFunction mf);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Class for the conclusion of a fuzzy rule       //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class InnerConclusion {
  private double degree;
  private InnerMembershipFunction mf;
  private InnerOperatorset op;

  InnerConclusion(double degree, InnerMembershipFunction mf, InnerOperatorset op) {
   this.op = op;
   this.degree = degree;
   this.mf = mf;
  }

  public double degree() { return degree; }
  public double compute(double x) { return op.imp(degree,mf.isEqual(x)); }
  public double center() { return mf.center(); }
  public double basis() { return mf.basis(); }
  public double param(int i) { return mf.param(i); }
  public double min() { return mf.min; }
  public double max() { return mf.max; }
  public double step() { return mf.step; }
  public boolean isSingleton() { 
   return mf.getClass().getName().endsWith("MF_xfl_singleton");
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function of an output variable      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OutputMembershipFunction implements MembershipFunction {
  public InnerConclusion[] conc;
  public double[] input;
  private InnerOperatorset op;

  OutputMembershipFunction() {
   this.conc = new InnerConclusion[0];
  }

  public void set(int size, InnerOperatorset op, double[] input) {
   this.input = input;
   this.op = op;
   this.conc = new InnerConclusion[size];
  }

  public void set(int pos, double dg, InnerMembershipFunction imf) {
   this.conc[pos] = new InnerConclusion(dg,imf,op);
  }

  public double compute(double x) {
   double dom = conc[0].compute(x);
   for(int i=1; i<conc.length; i++) dom = op.also(dom,conc[i].compute(x));
   return dom;
  }

  public double defuzzify() {
   return op.defuz(this);
  }

  public double min() {
   return conc[0].min();
  }

  public double max() {
   return conc[0].max();
  }

  public double step() {
   return conc[0].step();
  }

  public boolean isDiscrete() {
   for(int i=0; i<conc.length; i++) if(!conc[i].isSingleton()) return false;
   return true;
  }
 
  public double[][] getDiscreteValues() {
   double[][] value = new double[conc.length][2];
   for(int i=0; i<conc.length; i++) {
    value[i][0] = conc[i].param(0);
    value[i][1] = conc[i].degree();
   }
   return value;
  }

 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //      Membership function MF_xfl_trapezoid      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

  private class MF_xfl_trapezoid extends InnerMembershipFunction {
   double a;
   double b;
   double c;
   double d;

   MF_xfl_trapezoid( double min, double max, double step, double param[]){
    super.min = min;
    super.max = max;
    super.step = step;
    this.a = param[0];
    this.b = param[1];
    this.c = param[2];
    this.d = param[3];
   }
   double param(int _i) {
    switch(_i) {
     case 0: return a;
     case 1: return b;
     case 2: return c;
     case 3: return d;
     default: return 0;
    }
   }
   double isEqual(double x) {
    return (x<a || x>d? 0: (x<b? (x-a)/(b-a) : (x<c?1 : (d-x)/(d-c)))); 
   }
   double isGreaterOrEqual(double x) {
    return (x<a? 0 : (x>b? 1 : (x-a)/(b-a) )); 
   }
   double isSmallerOrEqual(double x) {
    return (x<c? 1 : (x>d? 0 : (d-x)/(d-c) )); 
   }
   double center() {
    return (b+c)/2; 
   }
   double basis() {
    return (d-a); 
   }
  }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP_operador      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP_operador extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
   double step = mf.step();
  double num=0, denom=0;
  for(double x=min; x<=max; x+=step) {
   double m = mf.compute(x);
   num += x*m;
   denom += m;
  }
  if(denom==0) return (min+max)/2;
  return num/denom;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //     Operator set OP__default_      //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class OP__default_ extends InnerOperatorset {
  double and(double a, double b) {
    return (a<b? a : b); 
  }
  double or(double a, double b) {
    return (a>b? a : b); 
  }
  double also(double a, double b) {
    return (a>b? a : b); 
  }
  double imp(double a, double b) {
    return (a<b? a : b); 
  }
  double not(double a) {
    return 1-a; 
  }
  double very(double a) {
   double w = 2.0;
    return Math.pow(a,w); 
  }
  double moreorless(double a) {
   double w = 0.5;
    return Math.pow(a,w); 
  }
  double slightly(double a) {
    return 4*a*(1-a); 
  }
 double defuz(OutputMembershipFunction mf) {
   double min = mf.min();
   double max = mf.max();
   double step = mf.step();
  double num=0, denom=0;
  for(double x=min; x<=max; x+=step) {
   double m = mf.compute(x);
   num += x*m;
   denom += m;
  }
  if(denom==0) return (min+max)/2;
  return num/denom;
  }
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeSistemAlertTemp  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeSistemAlertTemp {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeProteccMomentEmerg  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeProteccMomentEmerg {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeComunEntrenProteg  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeComunEntrenProteg {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeComunicacion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeComunicacion {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeBarreraNatural  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeBarreraNatural {
  private double min = 0.0;
  private double max = 5.0;
  private double step = 0.0196078431372549;
  double _p_Deficiente[] = { -1.5,0.0,1.5,3.0 };
  double _p_MedianamenteEficiente[] = { 0.0,1.5,3.0,4.5 };
  double _p_Eficiente[] = { 1.5,3.0,5.0,6.0 };
  MF_xfl_trapezoid Deficiente = new MF_xfl_trapezoid(min,max,step,_p_Deficiente);
  MF_xfl_trapezoid MedianamenteEficiente = new MF_xfl_trapezoid(min,max,step,_p_MedianamenteEficiente);
  MF_xfl_trapezoid Eficiente = new MF_xfl_trapezoid(min,max,step,_p_Eficiente);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeBarreraArtificial  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeBarreraArtificial {
  private double min = 0.0;
  private double max = 5.0;
  private double step = 0.0196078431372549;
  double _p_Deficiente[] = { -1.5,0.0,1.5,3.0 };
  double _p_MedianamenteEficiente[] = { 0.0,1.5,3.0,4.5 };
  double _p_Eficiente[] = { 1.5,3.0,5.0,6.0 };
  MF_xfl_trapezoid Deficiente = new MF_xfl_trapezoid(min,max,step,_p_Deficiente);
  MF_xfl_trapezoid MedianamenteEficiente = new MF_xfl_trapezoid(min,max,step,_p_MedianamenteEficiente);
  MF_xfl_trapezoid Eficiente = new MF_xfl_trapezoid(min,max,step,_p_Eficiente);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeEspacNaturProteg  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeEspacNaturProteg {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeEspeciesProteg  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeEspeciesProteg {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typePatrimHistArt  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typePatrimHistArt {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeEstadoFisicoConstruc  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeEstadoFisicoConstruc {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeDensidadPoblacion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeDensidadPoblacion {
  private double min = 0.0;
  private double max = 500.0;
  private double step = 1.9607843137254901;
  double _p_Baja[] = { -60.0,0.0,100.0,200.0 };
  double _p_Media[] = { 0.0,100.0,200.0,400.0 };
  double _p_Alta[] = { 100.0,200.0,500.0,600.0 };
  MF_xfl_trapezoid Baja = new MF_xfl_trapezoid(min,max,step,_p_Baja);
  MF_xfl_trapezoid Media = new MF_xfl_trapezoid(min,max,step,_p_Media);
  MF_xfl_trapezoid Alta = new MF_xfl_trapezoid(min,max,step,_p_Alta);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeAreaComunitaria  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeAreaComunitaria {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.5;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeAgravamientoImpacto  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeAgravamientoImpacto {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typePreparacion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typePreparacion {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.125,0.0,0.25,0.375 };
  double _p_Regular[] = { 0.25,0.375,0.625,0.75 };
  double _p_Mal[] = { 0.625,0.75,1.0,1.125 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeProteccion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeProteccion {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeReceptAmbientVulnerables  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeReceptAmbientVulnerables {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeFaltaHomeostasis  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeFaltaHomeostasis {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeServiciosSalv  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeServiciosSalv {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeBrigLuchaIncendios  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeBrigLuchaIncendios {
  private double min = 0.0;
  private double max = 60.0;
  private double step = 0.23529411764705882;
  double _p_MaxOperat[] = { -10.0,0.0,10.0,20.0 };
  double _p_MediaOperat[] = { 0.0,10.0,20.0,30.0 };
  double _p_BajaOperat[] = { 10.0,20.0,30.0,40.0 };
  MF_xfl_trapezoid MaxOperat = new MF_xfl_trapezoid(min,max,step,_p_MaxOperat);
  MF_xfl_trapezoid MediaOperat = new MF_xfl_trapezoid(min,max,step,_p_MediaOperat);
  MF_xfl_trapezoid BajaOperat = new MF_xfl_trapezoid(min,max,step,_p_BajaOperat);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeBomberos  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeBomberos {
  private double min = 0.0;
  private double max = 10.0;
  private double step = 0.0392156862745098;
  double _p_MaxOperat[] = { -3.0,0.0,3.0,5.0 };
  double _p_MediaOperat[] = { 0.0,3.0,5.0,7.0 };
  double _p_BajaOperat[] = { 3.0,5.0,10.0,20.0 };
  MF_xfl_trapezoid MaxOperat = new MF_xfl_trapezoid(min,max,step,_p_MaxOperat);
  MF_xfl_trapezoid MediaOperat = new MF_xfl_trapezoid(min,max,step,_p_MediaOperat);
  MF_xfl_trapezoid BajaOperat = new MF_xfl_trapezoid(min,max,step,_p_BajaOperat);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeAmbulancias  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeAmbulancias {
  private double min = 0.0;
  private double max = 10.0;
  private double step = 0.0392156862745098;
  double _p_MaxOperat[] = { -3.0,0.0,3.0,5.0 };
  double _p_MediaOperat[] = { 0.0,3.0,5.0,7.0 };
  double _p_BajaOperat[] = { 3.0,5.0,10.0,20.0 };
  MF_xfl_trapezoid MaxOperat = new MF_xfl_trapezoid(min,max,step,_p_MaxOperat);
  MF_xfl_trapezoid MediaOperat = new MF_xfl_trapezoid(min,max,step,_p_MediaOperat);
  MF_xfl_trapezoid BajaOperat = new MF_xfl_trapezoid(min,max,step,_p_BajaOperat);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typePrimAuxyOtros  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typePrimAuxyOtros {
  private double min = 0.0;
  private double max = 60.0;
  private double step = 0.23529411764705882;
  double _p_MaxOperat[] = { -10.0,0.0,10.0,20.0 };
  double _p_MediaOperat[] = { 0.0,10.0,20.0,30.0 };
  double _p_BajaOperat[] = { 10.0,20.0,60.0,70.0 };
  MF_xfl_trapezoid MaxOperat = new MF_xfl_trapezoid(min,max,step,_p_MaxOperat);
  MF_xfl_trapezoid MediaOperat = new MF_xfl_trapezoid(min,max,step,_p_MediaOperat);
  MF_xfl_trapezoid BajaOperat = new MF_xfl_trapezoid(min,max,step,_p_BajaOperat);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeOperatEmerg  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeOperatEmerg {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeServHospUrgenc  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeServHospUrgenc {
  private double min = 0.0;
  private double max = 10.0;
  private double step = 0.0392156862745098;
  double _p_Bien[] = { -3.0,0.0,3.0,5.0 };
  double _p_Regular[] = { 0.0,3.0,5.0,7.0 };
  double _p_Mal[] = { 3.0,5.0,10.0,15.0 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeRecuperBasica  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeRecuperBasica {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.125,0.0,0.25,0.375 };
  double _p_Regular[] = { 0.25,0.375,0.625,0.75 };
  double _p_Mal[] = { 0.625,0.75,1.0,1.125 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeLongevidad  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeLongevidad {
  private double min = 0.0;
  private double max = 100.0;
  private double step = 0.39215686274509803;
  double _p_Baja[] = { -10.0,0.0,65.0,70.0 };
  double _p_Media[] = { 0.0,65.0,70.0,100.0 };
  double _p_Alta[] = { 65.0,70.0,100.0,120.0 };
  MF_xfl_trapezoid Baja = new MF_xfl_trapezoid(min,max,step,_p_Baja);
  MF_xfl_trapezoid Media = new MF_xfl_trapezoid(min,max,step,_p_Media);
  MF_xfl_trapezoid Alta = new MF_xfl_trapezoid(min,max,step,_p_Alta);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeEscolaridad  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeEscolaridad {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Alta[] = { -0.125,0.0,0.25,0.375 };
  double _p_Media[] = { 0.25,0.375,0.625,0.75 };
  double _p_Baja[] = { 0.625,0.75,1.0,1.125 };
  MF_xfl_trapezoid Alta = new MF_xfl_trapezoid(min,max,step,_p_Alta);
  MF_xfl_trapezoid Media = new MF_xfl_trapezoid(min,max,step,_p_Media);
  MF_xfl_trapezoid Baja = new MF_xfl_trapezoid(min,max,step,_p_Baja);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeIngresoPerCap  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeIngresoPerCap {
  private double min = 0.0;
  private double max = 50.0;
  private double step = 0.19607843137254902;
  double _p_Mal[] = { -6.0,0.0,15.0,20.0 };
  double _p_Regular[] = { 0.0,15.0,20.0,25.0 };
  double _p_Bien[] = { 20.0,25.0,50.0,60.0 };
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeNivelDesarrHum  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeNivelDesarrHum {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Alto[] = { -0.125,0.0,0.25,0.375 };
  double _p_Medio[] = { 0.25,0.375,0.625,0.75 };
  double _p_Bajo[] = { 0.625,0.75,1.0,1.125 };
  MF_xfl_trapezoid Alto = new MF_xfl_trapezoid(min,max,step,_p_Alto);
  MF_xfl_trapezoid Medio = new MF_xfl_trapezoid(min,max,step,_p_Medio);
  MF_xfl_trapezoid Bajo = new MF_xfl_trapezoid(min,max,step,_p_Bajo);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeReconstruccion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeReconstruccion {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Type TP_typeFaltaResiliencia  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private class TP_typeFaltaResiliencia {
  private double min = 0.0;
  private double max = 1.0;
  private double step = 0.00392156862745098;
  double _p_Bien[] = { -0.3,0.0,0.3,0.6 };
  double _p_Regular[] = { 0.0,0.3,0.6,1.0 };
  double _p_Mal[] = { 0.3,0.6,1.0,1.3 };
  MF_xfl_trapezoid Bien = new MF_xfl_trapezoid(min,max,step,_p_Bien);
  MF_xfl_trapezoid Regular = new MF_xfl_trapezoid(min,max,step,_p_Regular);
  MF_xfl_trapezoid Mal = new MF_xfl_trapezoid(min,max,step,_p_Mal);
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Preparacion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Preparacion(MembershipFunction inSAT, MembershipFunction inPME, MembershipFunction inCEP, MembershipFunction inComunic) {
  double _rl;
  double _input[] = new double[4];
  if(inSAT instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inSAT).getValue();
  if(inPME instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) inPME).getValue();
  if(inCEP instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) inCEP).getValue();
  if(inComunic instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) inComunic).getValue();
  OP_operador _op = new OP_operador();
  OutputMembershipFunction outPrep = new OutputMembershipFunction();
  outPrep.set(9,_op,_input);
  TP_typeSistemAlertTemp _t_inSAT = new TP_typeSistemAlertTemp();
  TP_typeProteccMomentEmerg _t_inPME = new TP_typeProteccMomentEmerg();
  TP_typeComunEntrenProteg _t_inCEP = new TP_typeComunEntrenProteg();
  TP_typeComunicacion _t_inComunic = new TP_typeComunicacion();
  TP_typePreparacion _t_outPrep = new TP_typePreparacion();
  int _i_outPrep=0;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Bien.isEqual(inSAT),_t_inPME.Bien.isEqual(inPME)),_t_inCEP.Bien.isEqual(inCEP)),_t_inComunic.Bien.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Bien); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Mal.isEqual(inSAT),_t_inPME.Mal.isEqual(inPME)),_t_inCEP.Mal.isEqual(inCEP)),_t_inComunic.Mal.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Mal); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Regular.isEqual(inSAT),_t_inPME.Regular.isEqual(inPME)),_t_inCEP.Regular.isEqual(inCEP)),_t_inComunic.Regular.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Regular); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Bien.isEqual(inSAT),_t_inPME.Bien.isEqual(inPME)),_t_inCEP.Bien.isEqual(inCEP)),_t_inComunic.Regular.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Bien); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Bien.isEqual(inSAT),_t_inPME.Bien.isEqual(inPME)),_t_inCEP.Regular.isEqual(inCEP)),_t_inComunic.Regular.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Regular); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Bien.isEqual(inSAT),_t_inPME.Regular.isEqual(inPME)),_t_inCEP.Regular.isEqual(inCEP)),_t_inComunic.Regular.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Regular); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Regular.isEqual(inSAT),_t_inPME.Regular.isEqual(inPME)),_t_inCEP.Bien.isEqual(inCEP)),_t_inComunic.Bien.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Regular); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Regular.isEqual(inSAT),_t_inPME.Regular.isEqual(inPME)),_t_inCEP.Regular.isEqual(inCEP)),_t_inComunic.Mal.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Regular); _i_outPrep++;
  _rl = _op.and(_op.and(_op.and(_t_inSAT.Regular.isEqual(inSAT),_t_inPME.Regular.isEqual(inPME)),_t_inCEP.Mal.isEqual(inCEP)),_t_inComunic.Mal.isEqual(inComunic));
  outPrep.set(_i_outPrep,_rl, _t_outPrep.Mal); _i_outPrep++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outPrep.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Proteccion  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Proteccion(MembershipFunction inBN, MembershipFunction inBA) {
  double _rl;
  double _input[] = new double[2];
  if(inBN instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inBN).getValue();
  if(inBA instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) inBA).getValue();
  OP_operador _op = new OP_operador();
  OutputMembershipFunction outProtec = new OutputMembershipFunction();
  outProtec.set(9,_op,_input);
  TP_typeBarreraNatural _t_inBN = new TP_typeBarreraNatural();
  TP_typeBarreraArtificial _t_inBA = new TP_typeBarreraArtificial();
  TP_typeProteccion _t_outProtec = new TP_typeProteccion();
  int _i_outProtec=0;
  _rl = _op.and(_t_inBN.Eficiente.isEqual(inBN),_t_inBA.Eficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Bien); _i_outProtec++;
  _rl = _op.and(_t_inBN.Eficiente.isEqual(inBN),_t_inBA.MedianamenteEficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Regular); _i_outProtec++;
  _rl = _op.and(_t_inBN.MedianamenteEficiente.isEqual(inBN),_t_inBA.Eficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Regular); _i_outProtec++;
  _rl = _op.and(_t_inBN.MedianamenteEficiente.isEqual(inBN),_t_inBA.MedianamenteEficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Regular); _i_outProtec++;
  _rl = _op.and(_t_inBN.Eficiente.isEqual(inBN),_t_inBA.Deficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Regular); _i_outProtec++;
  _rl = _op.and(_t_inBN.Deficiente.isEqual(inBN),_t_inBA.Eficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Regular); _i_outProtec++;
  _rl = _op.and(_t_inBN.MedianamenteEficiente.isEqual(inBN),_t_inBA.Deficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Mal); _i_outProtec++;
  _rl = _op.and(_t_inBN.Deficiente.isEqual(inBN),_t_inBA.MedianamenteEficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Mal); _i_outProtec++;
  _rl = _op.and(_t_inBN.Deficiente.isEqual(inBN),_t_inBA.Deficiente.isEqual(inBA));
  outProtec.set(_i_outProtec,_rl, _t_outProtec.Mal); _i_outProtec++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outProtec.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_ReceptAmbientVulnerables  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_ReceptAmbientVulnerables(MembershipFunction inENP, MembershipFunction inEP, MembershipFunction inPHA) {
  double _rl;
  double _input[] = new double[3];
  if(inENP instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inENP).getValue();
  if(inEP instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) inEP).getValue();
  if(inPHA instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) inPHA).getValue();
  OP_operador _op = new OP_operador();
  OutputMembershipFunction outRAVul = new OutputMembershipFunction();
  outRAVul.set(12,_op,_input);
  TP_typeEspacNaturProteg _t_inENP = new TP_typeEspacNaturProteg();
  TP_typeEspeciesProteg _t_inEP = new TP_typeEspeciesProteg();
  TP_typePatrimHistArt _t_inPHA = new TP_typePatrimHistArt();
  TP_typeReceptAmbientVulnerables _t_outRAVul = new TP_typeReceptAmbientVulnerables();
  int _i_outRAVul=0;
  _rl = _op.and(_op.and(_t_inENP.Bien.isEqual(inENP),_t_inEP.Bien.isEqual(inEP)),_t_inPHA.Bien.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Bien); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Bien.isEqual(inENP),_t_inEP.Bien.isEqual(inEP)),_t_inPHA.Regular.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Bien); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Bien.isEqual(inENP),_t_inEP.Regular.isEqual(inEP)),_t_inPHA.Bien.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Bien); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Regular.isEqual(inENP),_t_inEP.Bien.isEqual(inEP)),_t_inPHA.Bien.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Bien); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Bien.isEqual(inENP),_t_inEP.Regular.isEqual(inEP)),_t_inPHA.Regular.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Regular); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Regular.isEqual(inENP),_t_inEP.Bien.isEqual(inEP)),_t_inPHA.Regular.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Regular); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Regular.isEqual(inENP),_t_inEP.Regular.isEqual(inEP)),_t_inPHA.Bien.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Regular); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Regular.isEqual(inENP),_t_inEP.Regular.isEqual(inEP)),_t_inPHA.Regular.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Regular); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Regular.isEqual(inENP),_t_inEP.Regular.isEqual(inEP)),_t_inPHA.Mal.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Mal); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Regular.isEqual(inENP),_t_inEP.Mal.isEqual(inEP)),_t_inPHA.Regular.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Mal); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Mal.isEqual(inENP),_t_inEP.Regular.isEqual(inEP)),_t_inPHA.Regular.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Mal); _i_outRAVul++;
  _rl = _op.and(_op.and(_t_inENP.Mal.isEqual(inENP),_t_inEP.Mal.isEqual(inEP)),_t_inPHA.Mal.isEqual(inPHA));
  outRAVul.set(_i_outRAVul,_rl, _t_outRAVul.Mal); _i_outRAVul++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outRAVul.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Falta_de_Homeostasis  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Falta_de_Homeostasis(MembershipFunction outPrep, MembershipFunction outProtec, MembershipFunction outRAVul, MembershipFunction inEFC, MembershipFunction inDP, MembershipFunction inAC) {
  double _rl;
  double _input[] = new double[6];
  if(outPrep instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) outPrep).getValue();
  if(outProtec instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) outProtec).getValue();
  if(outRAVul instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) outRAVul).getValue();
  if(inEFC instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) inEFC).getValue();
  if(inDP instanceof FuzzySingleton)
   _input[4] = ((FuzzySingleton) inDP).getValue();
  if(inAC instanceof FuzzySingleton)
   _input[5] = ((FuzzySingleton) inAC).getValue();
  OP_operador _op = new OP_operador();
  OutputMembershipFunction outFaltaHomeostasis = new OutputMembershipFunction();
  outFaltaHomeostasis.set(6,_op,_input);
  TP_typePreparacion _t_outPrep = new TP_typePreparacion();
  TP_typeProteccion _t_outProtec = new TP_typeProteccion();
  TP_typeReceptAmbientVulnerables _t_outRAVul = new TP_typeReceptAmbientVulnerables();
  TP_typeEstadoFisicoConstruc _t_inEFC = new TP_typeEstadoFisicoConstruc();
  TP_typeDensidadPoblacion _t_inDP = new TP_typeDensidadPoblacion();
  TP_typeAreaComunitaria _t_inAC = new TP_typeAreaComunitaria();
  TP_typeFaltaHomeostasis _t_outFaltaHomeostasis = new TP_typeFaltaHomeostasis();
  int _i_outFaltaHomeostasis=0;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_outPrep.Bien.isEqual(outPrep),_t_outProtec.Bien.isEqual(outProtec)),_t_outRAVul.Bien.isEqual(outRAVul)),_t_inEFC.Bien.isEqual(inEFC)),_t_inDP.Baja.isEqual(inDP)),_t_inAC.Bien.isEqual(inAC));
  outFaltaHomeostasis.set(_i_outFaltaHomeostasis,_rl, _t_outFaltaHomeostasis.Bien); _i_outFaltaHomeostasis++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_outPrep.Regular.isEqual(outPrep),_t_outProtec.Regular.isEqual(outProtec)),_t_outRAVul.Regular.isEqual(outRAVul)),_t_inEFC.Regular.isEqual(inEFC)),_t_inDP.Media.isEqual(inDP)),_t_inAC.Regular.isEqual(inAC));
  outFaltaHomeostasis.set(_i_outFaltaHomeostasis,_rl, _t_outFaltaHomeostasis.Regular); _i_outFaltaHomeostasis++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_outPrep.Mal.isEqual(outPrep),_t_outProtec.Mal.isEqual(outProtec)),_t_outRAVul.Mal.isEqual(outRAVul)),_t_inEFC.Mal.isEqual(inEFC)),_t_inDP.Alta.isEqual(inDP)),_t_inAC.Mal.isEqual(inAC));
  outFaltaHomeostasis.set(_i_outFaltaHomeostasis,_rl, _t_outFaltaHomeostasis.Mal); _i_outFaltaHomeostasis++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_outPrep.Bien.isEqual(outPrep),_t_outProtec.Bien.isEqual(outProtec)),_t_outRAVul.Bien.isEqual(outRAVul)),_t_inEFC.Regular.isEqual(inEFC)),_t_inDP.Media.isEqual(inDP)),_t_inAC.Regular.isEqual(inAC));
  outFaltaHomeostasis.set(_i_outFaltaHomeostasis,_rl, _t_outFaltaHomeostasis.Regular); _i_outFaltaHomeostasis++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_outPrep.Mal.isEqual(outPrep),_t_outProtec.Mal.isEqual(outProtec)),_t_outRAVul.Mal.isEqual(outRAVul)),_t_inEFC.Regular.isEqual(inEFC)),_t_inDP.Media.isEqual(inDP)),_t_inAC.Regular.isEqual(inAC));
  outFaltaHomeostasis.set(_i_outFaltaHomeostasis,_rl, _t_outFaltaHomeostasis.Mal); _i_outFaltaHomeostasis++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_outPrep.Bien.isEqual(outPrep),_t_outProtec.Regular.isEqual(outProtec)),_t_outRAVul.Bien.isEqual(outRAVul)),_t_inEFC.Bien.isEqual(inEFC)),_t_inDP.Baja.isEqual(inDP)),_t_inAC.Bien.isEqual(inAC));
  outFaltaHomeostasis.set(_i_outFaltaHomeostasis,_rl, _t_outFaltaHomeostasis.Bien); _i_outFaltaHomeostasis++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outFaltaHomeostasis.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_OperatividadEmergencias  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_OperatividadEmergencias(MembershipFunction inBLI, MembershipFunction inBomb, MembershipFunction inAmbul, MembershipFunction inPAYO) {
  double _rl;
  double _input[] = new double[4];
  if(inBLI instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inBLI).getValue();
  if(inBomb instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) inBomb).getValue();
  if(inAmbul instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) inAmbul).getValue();
  if(inPAYO instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) inPAYO).getValue();
  OP_operador _op = new OP_operador();
  OutputMembershipFunction outOE = new OutputMembershipFunction();
  outOE.set(17,_op,_input);
  TP_typeBrigLuchaIncendios _t_inBLI = new TP_typeBrigLuchaIncendios();
  TP_typeBomberos _t_inBomb = new TP_typeBomberos();
  TP_typeAmbulancias _t_inAmbul = new TP_typeAmbulancias();
  TP_typePrimAuxyOtros _t_inPAYO = new TP_typePrimAuxyOtros();
  TP_typeOperatEmerg _t_outOE = new TP_typeOperatEmerg();
  int _i_outOE=0;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MaxOperat.isEqual(inBLI),_t_inBomb.MaxOperat.isEqual(inBomb)),_t_inAmbul.MaxOperat.isEqual(inAmbul)),_t_inPAYO.MaxOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Bien); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MaxOperat.isEqual(inBLI),_t_inBomb.MaxOperat.isEqual(inBomb)),_t_inAmbul.MaxOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Bien); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MaxOperat.isEqual(inBLI),_t_inBomb.MaxOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MaxOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Bien); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MaxOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.MaxOperat.isEqual(inAmbul)),_t_inPAYO.MaxOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Bien); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.MaxOperat.isEqual(inBomb)),_t_inAmbul.MaxOperat.isEqual(inAmbul)),_t_inPAYO.MaxOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Bien); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MaxOperat.isEqual(inBLI),_t_inBomb.MaxOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MaxOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.BajaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.BajaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.BajaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.BajaOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Regular); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.BajaOperat.isEqual(inAmbul)),_t_inPAYO.BajaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Mal); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.MediaOperat.isEqual(inBLI),_t_inBomb.BajaOperat.isEqual(inBomb)),_t_inAmbul.BajaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Mal); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.BajaOperat.isEqual(inBLI),_t_inBomb.BajaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.MediaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Mal); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.BajaOperat.isEqual(inBLI),_t_inBomb.MediaOperat.isEqual(inBomb)),_t_inAmbul.MediaOperat.isEqual(inAmbul)),_t_inPAYO.BajaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Mal); _i_outOE++;
  _rl = _op.and(_op.and(_op.and(_t_inBLI.BajaOperat.isEqual(inBLI),_t_inBomb.BajaOperat.isEqual(inBomb)),_t_inAmbul.BajaOperat.isEqual(inAmbul)),_t_inPAYO.BajaOperat.isEqual(inPAYO));
  outOE.set(_i_outOE,_rl, _t_outOE.Mal); _i_outOE++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outOE.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_NivelDesarrolloHumano  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_NivelDesarrolloHumano(MembershipFunction inLong, MembershipFunction inEscol, MembershipFunction inIPC) {
  double _rl;
  double _input[] = new double[3];
  if(inLong instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inLong).getValue();
  if(inEscol instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) inEscol).getValue();
  if(inIPC instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) inIPC).getValue();
  OP__default_ _op = new OP__default_();
  OutputMembershipFunction outNivDH = new OutputMembershipFunction();
  outNivDH.set(8,_op,_input);
  TP_typeLongevidad _t_inLong = new TP_typeLongevidad();
  TP_typeEscolaridad _t_inEscol = new TP_typeEscolaridad();
  TP_typeIngresoPerCap _t_inIPC = new TP_typeIngresoPerCap();
  TP_typeNivelDesarrHum _t_outNivDH = new TP_typeNivelDesarrHum();
  int _i_outNivDH=0;
  _rl = _op.and(_op.and(_t_inLong.Baja.isEqual(inLong),_t_inEscol.Alta.isEqual(inEscol)),_t_inIPC.Bien.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Alto); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Baja.isEqual(inLong),_t_inEscol.Media.isEqual(inEscol)),_t_inIPC.Regular.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Medio); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Media.isEqual(inLong),_t_inEscol.Media.isEqual(inEscol)),_t_inIPC.Regular.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Medio); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Media.isEqual(inLong),_t_inEscol.Alta.isEqual(inEscol)),_t_inIPC.Bien.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Alto); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Alta.isEqual(inLong),_t_inEscol.Media.isEqual(inEscol)),_t_inIPC.Regular.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Medio); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Alta.isEqual(inLong),_t_inEscol.Baja.isEqual(inEscol)),_t_inIPC.Mal.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Bajo); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Alta.isEqual(inLong),_t_inEscol.Baja.isEqual(inEscol)),_t_inIPC.Regular.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Bajo); _i_outNivDH++;
  _rl = _op.and(_op.and(_t_inLong.Media.isEqual(inLong),_t_inEscol.Baja.isEqual(inEscol)),_t_inIPC.Mal.isEqual(inIPC));
  outNivDH.set(_i_outNivDH,_rl, _t_outNivDH.Bajo); _i_outNivDH++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = outNivDH;
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Falta_de_Resiliencia  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Falta_de_Resiliencia(MembershipFunction inSS, MembershipFunction outOE, MembershipFunction inSHU, MembershipFunction inRB, MembershipFunction outNivDH, MembershipFunction inRConst) {
  double _rl;
  double _input[] = new double[6];
  if(inSS instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) inSS).getValue();
  if(outOE instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) outOE).getValue();
  if(inSHU instanceof FuzzySingleton)
   _input[2] = ((FuzzySingleton) inSHU).getValue();
  if(inRB instanceof FuzzySingleton)
   _input[3] = ((FuzzySingleton) inRB).getValue();
  if(outNivDH instanceof FuzzySingleton)
   _input[4] = ((FuzzySingleton) outNivDH).getValue();
  if(inRConst instanceof FuzzySingleton)
   _input[5] = ((FuzzySingleton) inRConst).getValue();
  OP_operador _op = new OP_operador();
  OutputMembershipFunction outFaltaResiliencia = new OutputMembershipFunction();
  outFaltaResiliencia.set(9,_op,_input);
  TP_typeServiciosSalv _t_inSS = new TP_typeServiciosSalv();
  TP_typeOperatEmerg _t_outOE = new TP_typeOperatEmerg();
  TP_typeServHospUrgenc _t_inSHU = new TP_typeServHospUrgenc();
  TP_typeRecuperBasica _t_inRB = new TP_typeRecuperBasica();
  TP_typeNivelDesarrHum _t_outNivDH = new TP_typeNivelDesarrHum();
  TP_typeReconstruccion _t_inRConst = new TP_typeReconstruccion();
  TP_typeFaltaResiliencia _t_outFaltaResiliencia = new TP_typeFaltaResiliencia();
  int _i_outFaltaResiliencia=0;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Bien.isEqual(inSS),_t_outOE.Bien.isEqual(outOE)),_t_inSHU.Bien.isEqual(inSHU)),_t_inRB.Bien.isEqual(inRB)),_t_outNivDH.Alto.isEqual(outNivDH)),_t_inRConst.Bien.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Bien); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Bien.isEqual(inSS),_t_outOE.Bien.isEqual(outOE)),_t_inSHU.Bien.isEqual(inSHU)),_t_inRB.Regular.isEqual(inRB)),_t_outNivDH.Medio.isEqual(outNivDH)),_t_inRConst.Regular.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Regular); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Bien.isEqual(inSS),_t_outOE.Bien.isEqual(outOE)),_t_inSHU.Regular.isEqual(inSHU)),_t_inRB.Regular.isEqual(inRB)),_t_outNivDH.Medio.isEqual(outNivDH)),_t_inRConst.Bien.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Regular); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Bien.isEqual(inSS),_t_outOE.Regular.isEqual(outOE)),_t_inSHU.Bien.isEqual(inSHU)),_t_inRB.Bien.isEqual(inRB)),_t_outNivDH.Alto.isEqual(outNivDH)),_t_inRConst.Bien.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Bien); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Regular.isEqual(inSS),_t_outOE.Regular.isEqual(outOE)),_t_inSHU.Regular.isEqual(inSHU)),_t_inRB.Regular.isEqual(inRB)),_t_outNivDH.Medio.isEqual(outNivDH)),_t_inRConst.Regular.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Regular); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Regular.isEqual(inSS),_t_outOE.Regular.isEqual(outOE)),_t_inSHU.Regular.isEqual(inSHU)),_t_inRB.Regular.isEqual(inRB)),_t_outNivDH.Medio.isEqual(outNivDH)),_t_inRConst.Mal.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Regular); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Regular.isEqual(inSS),_t_outOE.Regular.isEqual(outOE)),_t_inSHU.Regular.isEqual(inSHU)),_t_inRB.Mal.isEqual(inRB)),_t_outNivDH.Bajo.isEqual(outNivDH)),_t_inRConst.Mal.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Mal); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Regular.isEqual(inSS),_t_outOE.Regular.isEqual(outOE)),_t_inSHU.Regular.isEqual(inSHU)),_t_inRB.Mal.isEqual(inRB)),_t_outNivDH.Bajo.isEqual(outNivDH)),_t_inRConst.Regular.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Mal); _i_outFaltaResiliencia++;
  _rl = _op.and(_op.and(_op.and(_op.and(_op.and(_t_inSS.Mal.isEqual(inSS),_t_outOE.Regular.isEqual(outOE)),_t_inSHU.Mal.isEqual(inSHU)),_t_inRB.Mal.isEqual(inRB)),_t_outNivDH.Bajo.isEqual(outNivDH)),_t_inRConst.Regular.isEqual(inRConst));
  outFaltaResiliencia.set(_i_outFaltaResiliencia,_rl, _t_outFaltaResiliencia.Mal); _i_outFaltaResiliencia++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = new FuzzySingleton(outFaltaResiliencia.defuzzify());
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //  Rulebase RL_Agravamiento_del_Impacto  //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 private MembershipFunction[] RL_Agravamiento_del_Impacto(MembershipFunction outFaltaHomeostasis, MembershipFunction outFaltaResiliencia) {
  double _rl;
  double _input[] = new double[2];
  if(outFaltaHomeostasis instanceof FuzzySingleton)
   _input[0] = ((FuzzySingleton) outFaltaHomeostasis).getValue();
  if(outFaltaResiliencia instanceof FuzzySingleton)
   _input[1] = ((FuzzySingleton) outFaltaResiliencia).getValue();
  OP__default_ _op = new OP__default_();
  OutputMembershipFunction outAI = new OutputMembershipFunction();
  outAI.set(9,_op,_input);
  TP_typeFaltaHomeostasis _t_outFaltaHomeostasis = new TP_typeFaltaHomeostasis();
  TP_typeFaltaResiliencia _t_outFaltaResiliencia = new TP_typeFaltaResiliencia();
  TP_typeAgravamientoImpacto _t_outAI = new TP_typeAgravamientoImpacto();
  int _i_outAI=0;
  _rl = _op.and(_t_outFaltaHomeostasis.Bien.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Bien.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Bien); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Bien.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Regular.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Regular); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Regular.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Bien.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Regular); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Bien.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Mal.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Regular); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Mal.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Bien.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Regular); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Regular.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Regular.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Regular); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Regular.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Mal.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Mal); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Mal.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Regular.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Mal); _i_outAI++;
  _rl = _op.and(_t_outFaltaHomeostasis.Mal.isEqual(outFaltaHomeostasis),_t_outFaltaResiliencia.Mal.isEqual(outFaltaResiliencia));
  outAI.set(_i_outAI,_rl, _t_outAI.Mal); _i_outAI++;
  MembershipFunction[] _output = new MembershipFunction[1];
  _output[0] = outAI;
  return _output;
 }

 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//
 //               Fuzzy Inference Engine                //
 //+++++++++++++++++++++++++++++++++++++++++++++++++++++//

 public double[] crispInference(double[] _input) {
  MembershipFunction inSAT = new FuzzySingleton(_input[0]);
  MembershipFunction inPME = new FuzzySingleton(_input[1]);
  MembershipFunction inCEP = new FuzzySingleton(_input[2]);
  MembershipFunction inComunic = new FuzzySingleton(_input[3]);
  MembershipFunction inBN = new FuzzySingleton(_input[4]);
  MembershipFunction inBA = new FuzzySingleton(_input[5]);
  MembershipFunction inENP = new FuzzySingleton(_input[6]);
  MembershipFunction inEP = new FuzzySingleton(_input[7]);
  MembershipFunction inPHA = new FuzzySingleton(_input[8]);
  MembershipFunction inEFC = new FuzzySingleton(_input[9]);
  MembershipFunction inDP = new FuzzySingleton(_input[10]);
  MembershipFunction inAC = new FuzzySingleton(_input[11]);
  MembershipFunction inSS = new FuzzySingleton(_input[12]);
  MembershipFunction inBLI = new FuzzySingleton(_input[13]);
  MembershipFunction inBomb = new FuzzySingleton(_input[14]);
  MembershipFunction inAmbul = new FuzzySingleton(_input[15]);
  MembershipFunction inPAYO = new FuzzySingleton(_input[16]);
  MembershipFunction inSHU = new FuzzySingleton(_input[17]);
  MembershipFunction inRB = new FuzzySingleton(_input[18]);
  MembershipFunction inLong = new FuzzySingleton(_input[19]);
  MembershipFunction inEscol = new FuzzySingleton(_input[20]);
  MembershipFunction inIPC = new FuzzySingleton(_input[21]);
  MembershipFunction inRConst = new FuzzySingleton(_input[22]);
  MembershipFunction outAI;
  MembershipFunction outPrep;
  MembershipFunction outProtec;
  MembershipFunction outRAVul;
  MembershipFunction outFaltaHomeostasis;
  MembershipFunction outOE;
  MembershipFunction outNivDH;
  MembershipFunction outFaltaResiliencia;
  MembershipFunction[] _call;
  _call = RL_Preparacion(inSAT,inPME,inCEP,inComunic); outPrep=_call[0];
  _call = RL_Proteccion(inBN,inBA); outProtec=_call[0];
  _call = RL_ReceptAmbientVulnerables(inENP,inEP,inPHA); outRAVul=_call[0];
  _call = RL_Falta_de_Homeostasis(outPrep,outProtec,outRAVul,inEFC,inDP,inAC); outFaltaHomeostasis=_call[0];
  _call = RL_OperatividadEmergencias(inBLI,inBomb,inAmbul,inPAYO); outOE=_call[0];
  _call = RL_NivelDesarrolloHumano(inLong,inEscol,inIPC); outNivDH=_call[0];
  _call = RL_Falta_de_Resiliencia(inSS,outOE,inSHU,inRB,outNivDH,inRConst); outFaltaResiliencia=_call[0];
  _call = RL_Agravamiento_del_Impacto(outFaltaHomeostasis,outFaltaResiliencia); outAI=_call[0];
  double _output[] = new double[1];
  if(outAI instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) outAI).getValue();
  else _output[0] = ((OutputMembershipFunction) outAI).defuzzify();
  return _output;
 }

 public double[] crispInference(MembershipFunction[] _input) {
  MembershipFunction inSAT = _input[0];
  MembershipFunction inPME = _input[1];
  MembershipFunction inCEP = _input[2];
  MembershipFunction inComunic = _input[3];
  MembershipFunction inBN = _input[4];
  MembershipFunction inBA = _input[5];
  MembershipFunction inENP = _input[6];
  MembershipFunction inEP = _input[7];
  MembershipFunction inPHA = _input[8];
  MembershipFunction inEFC = _input[9];
  MembershipFunction inDP = _input[10];
  MembershipFunction inAC = _input[11];
  MembershipFunction inSS = _input[12];
  MembershipFunction inBLI = _input[13];
  MembershipFunction inBomb = _input[14];
  MembershipFunction inAmbul = _input[15];
  MembershipFunction inPAYO = _input[16];
  MembershipFunction inSHU = _input[17];
  MembershipFunction inRB = _input[18];
  MembershipFunction inLong = _input[19];
  MembershipFunction inEscol = _input[20];
  MembershipFunction inIPC = _input[21];
  MembershipFunction inRConst = _input[22];
  MembershipFunction outAI;
  MembershipFunction outPrep;
  MembershipFunction outProtec;
  MembershipFunction outRAVul;
  MembershipFunction outFaltaHomeostasis;
  MembershipFunction outOE;
  MembershipFunction outNivDH;
  MembershipFunction outFaltaResiliencia;
  MembershipFunction[] _call;
  _call = RL_Preparacion(inSAT,inPME,inCEP,inComunic); outPrep=_call[0];
  _call = RL_Proteccion(inBN,inBA); outProtec=_call[0];
  _call = RL_ReceptAmbientVulnerables(inENP,inEP,inPHA); outRAVul=_call[0];
  _call = RL_Falta_de_Homeostasis(outPrep,outProtec,outRAVul,inEFC,inDP,inAC); outFaltaHomeostasis=_call[0];
  _call = RL_OperatividadEmergencias(inBLI,inBomb,inAmbul,inPAYO); outOE=_call[0];
  _call = RL_NivelDesarrolloHumano(inLong,inEscol,inIPC); outNivDH=_call[0];
  _call = RL_Falta_de_Resiliencia(inSS,outOE,inSHU,inRB,outNivDH,inRConst); outFaltaResiliencia=_call[0];
  _call = RL_Agravamiento_del_Impacto(outFaltaHomeostasis,outFaltaResiliencia); outAI=_call[0];
  double _output[] = new double[1];
  if(outAI instanceof FuzzySingleton)
   _output[0] = ((FuzzySingleton) outAI).getValue();
  else _output[0] = ((OutputMembershipFunction) outAI).defuzzify();
  return _output;
 }

 public MembershipFunction[] fuzzyInference(double[] _input) {
  MembershipFunction inSAT = new FuzzySingleton(_input[0]);
  MembershipFunction inPME = new FuzzySingleton(_input[1]);
  MembershipFunction inCEP = new FuzzySingleton(_input[2]);
  MembershipFunction inComunic = new FuzzySingleton(_input[3]);
  MembershipFunction inBN = new FuzzySingleton(_input[4]);
  MembershipFunction inBA = new FuzzySingleton(_input[5]);
  MembershipFunction inENP = new FuzzySingleton(_input[6]);
  MembershipFunction inEP = new FuzzySingleton(_input[7]);
  MembershipFunction inPHA = new FuzzySingleton(_input[8]);
  MembershipFunction inEFC = new FuzzySingleton(_input[9]);
  MembershipFunction inDP = new FuzzySingleton(_input[10]);
  MembershipFunction inAC = new FuzzySingleton(_input[11]);
  MembershipFunction inSS = new FuzzySingleton(_input[12]);
  MembershipFunction inBLI = new FuzzySingleton(_input[13]);
  MembershipFunction inBomb = new FuzzySingleton(_input[14]);
  MembershipFunction inAmbul = new FuzzySingleton(_input[15]);
  MembershipFunction inPAYO = new FuzzySingleton(_input[16]);
  MembershipFunction inSHU = new FuzzySingleton(_input[17]);
  MembershipFunction inRB = new FuzzySingleton(_input[18]);
  MembershipFunction inLong = new FuzzySingleton(_input[19]);
  MembershipFunction inEscol = new FuzzySingleton(_input[20]);
  MembershipFunction inIPC = new FuzzySingleton(_input[21]);
  MembershipFunction inRConst = new FuzzySingleton(_input[22]);
  MembershipFunction outAI;
  MembershipFunction outPrep;
  MembershipFunction outProtec;
  MembershipFunction outRAVul;
  MembershipFunction outFaltaHomeostasis;
  MembershipFunction outOE;
  MembershipFunction outNivDH;
  MembershipFunction outFaltaResiliencia;
  MembershipFunction[] _call;
  _call = RL_Preparacion(inSAT,inPME,inCEP,inComunic); outPrep=_call[0];
  _call = RL_Proteccion(inBN,inBA); outProtec=_call[0];
  _call = RL_ReceptAmbientVulnerables(inENP,inEP,inPHA); outRAVul=_call[0];
  _call = RL_Falta_de_Homeostasis(outPrep,outProtec,outRAVul,inEFC,inDP,inAC); outFaltaHomeostasis=_call[0];
  _call = RL_OperatividadEmergencias(inBLI,inBomb,inAmbul,inPAYO); outOE=_call[0];
  _call = RL_NivelDesarrolloHumano(inLong,inEscol,inIPC); outNivDH=_call[0];
  _call = RL_Falta_de_Resiliencia(inSS,outOE,inSHU,inRB,outNivDH,inRConst); outFaltaResiliencia=_call[0];
  _call = RL_Agravamiento_del_Impacto(outFaltaHomeostasis,outFaltaResiliencia); outAI=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = outAI;
  return _output;
 }

 public MembershipFunction[] fuzzyInference(MembershipFunction[] _input) {
  MembershipFunction inSAT = _input[0];
  MembershipFunction inPME = _input[1];
  MembershipFunction inCEP = _input[2];
  MembershipFunction inComunic = _input[3];
  MembershipFunction inBN = _input[4];
  MembershipFunction inBA = _input[5];
  MembershipFunction inENP = _input[6];
  MembershipFunction inEP = _input[7];
  MembershipFunction inPHA = _input[8];
  MembershipFunction inEFC = _input[9];
  MembershipFunction inDP = _input[10];
  MembershipFunction inAC = _input[11];
  MembershipFunction inSS = _input[12];
  MembershipFunction inBLI = _input[13];
  MembershipFunction inBomb = _input[14];
  MembershipFunction inAmbul = _input[15];
  MembershipFunction inPAYO = _input[16];
  MembershipFunction inSHU = _input[17];
  MembershipFunction inRB = _input[18];
  MembershipFunction inLong = _input[19];
  MembershipFunction inEscol = _input[20];
  MembershipFunction inIPC = _input[21];
  MembershipFunction inRConst = _input[22];
  MembershipFunction outAI;
  MembershipFunction outPrep;
  MembershipFunction outProtec;
  MembershipFunction outRAVul;
  MembershipFunction outFaltaHomeostasis;
  MembershipFunction outOE;
  MembershipFunction outNivDH;
  MembershipFunction outFaltaResiliencia;
  MembershipFunction[] _call;
  _call = RL_Preparacion(inSAT,inPME,inCEP,inComunic); outPrep=_call[0];
  _call = RL_Proteccion(inBN,inBA); outProtec=_call[0];
  _call = RL_ReceptAmbientVulnerables(inENP,inEP,inPHA); outRAVul=_call[0];
  _call = RL_Falta_de_Homeostasis(outPrep,outProtec,outRAVul,inEFC,inDP,inAC); outFaltaHomeostasis=_call[0];
  _call = RL_OperatividadEmergencias(inBLI,inBomb,inAmbul,inPAYO); outOE=_call[0];
  _call = RL_NivelDesarrolloHumano(inLong,inEscol,inIPC); outNivDH=_call[0];
  _call = RL_Falta_de_Resiliencia(inSS,outOE,inSHU,inRB,outNivDH,inRConst); outFaltaResiliencia=_call[0];
  _call = RL_Agravamiento_del_Impacto(outFaltaHomeostasis,outFaltaResiliencia); outAI=_call[0];
  MembershipFunction _output[] = new MembershipFunction[1];
  _output[0] = outAI;
  return _output;
 }

}
