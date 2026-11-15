"""Gera o PDF vetorial sem dependências externas, a partir dos mapeamentos JPA."""
from pathlib import Path
import re
ROOT = Path(__file__).resolve().parents[1]
W,H=1191,842
pages=[]; ops=[]
def raw(s): ops.append(s)
def color(c): return ' '.join(str(x) for x in c)
def rect(x,y,w,h,fill=(.95,.97,.99)):
 raw(f'{color(fill)} rg {x} {H-y-h} {w} {h} re f')
def text(x,y,s,size=11,bold=False,c=(.12,.18,.26)):
 s=s.replace('\\','\\\\').replace('(','\\(').replace(')','\\)')
 raw(f'BT /F{2 if bold else 1} {size} Tf {color(c)} rg 1 0 0 1 {x} {H-y-size} Tm ({s}) Tj ET')
def line(points,c=(.35,.47,.57),dash=False):
 raw(f'{color(c)} RG 1.2 w '+ ('[5 4] 0 d ' if dash else '[] 0 d ')+f'{points[0][0]} {H-points[0][1]} m '+' '.join(f'{x} {H-y} l' for x,y in points[1:])+' S')
def page(title,sub):
 global ops
 ops=[]; rect(0,0,W,7,(.05,.48,.52)); text(35,24,title,24,True); text(35,60,sub,11)
def finish(n):
 text(35,811,'Projeto-TCC_v2  |  Fonte: entidades JPA em src/main/java/com/api/Projeto_3/model  |  08/11/2026',9)
 text(1110,811,f'{n} / 6',9); pages.append('\n'.join(ops).encode('cp1252','replace'))
def box(x,y,w,title,rows):
 rect(x,y,w,29,(.08,.26,.35)); text(x+10,y+6,title,12,True,(1,1,1)); rect(x,y+29,w,18*len(rows)+9)
 for i,r in enumerate(rows): text(x+10,y+35+i*18,r,10)
def edge(points,label,lx,ly,dash=False):
 line(points,dash=dash); text(lx,ly,label,10,True)
def note(y,title,lines):
 rect(35,y,1120,38+19*len(lines),(.94,.97,.98))
 text(51,y+12,title,12,True)
 for i,t in enumerate(lines): text(51,y+37+i*19,t,11)
def relation(points,start,end,label,lx,ly,dash=False):
 line(points,dash=dash)
 x,y=points[0]; nx,ny=points[1]
 text(x+7 if nx>=x else x-40,y+6 if ny>y else y-19,start,11,True)
 x,y=points[-1]; px,py=points[-2]
 text(x-40 if px<x else x+7,y-19,end,11,True)
 text(lx,ly,label,11)
page('01  |  Perfis, acesso e cadastro','DIAGRAMA DE RELACIONAMENTOS  /  Cadastro e especialização dos usuários')
# Cardinalities appear beside their respective entities.
relation([(290,200),(445,200)],'0..1','0..1','fal_fk',340,175)
relation([(290,350),(365,350),(365,260),(445,260)],'0..1','0..1','mord',315,320)
relation([(745,200),(900,200)],'0..N','0..1','role_id',800,175)
relation([(495,327),(495,405),(210,405),(210,485)],'1','0..1','Herança JOINED',240,380,True)
relation([(595,327),(595,485)],'1','0..1','Herança JOINED',610,415,True)
relation([(695,327),(695,405),(1000,405),(1000,485)],'1','0..1','Herança JOINED',805,380,True)
box(35,145,255,'AFLIACAO',['PK  id','Dados dos pais / responsáveis'])
box(35,300,255,'MORADIA',['PK  id','Endereço do perfil'])
box(445,145,300,'PerfisModelo',['PK  id','FK  fal_fk  ->  AFLIACAO','FK  mord  ->  MORADIA','FK  role_id  ->  roles_tb','CPF único e obrigatório','Dados pessoais e credenciais','Atributos comuns aos três perfis','Herança com tabelas separadas'])
box(900,145,255,'roles_tb',['PK  id','authority'])
box(70,485,280,'ATLETAS',['PK/FK  id  ->  PerfisModelo','FK  atleta_trenador_fk','FK  atleta_medico_fk'])
box(455,485,280,'MEDICOS',['PK/FK  id  ->  PerfisModelo','Dados pessoais herdados'])
box(840,485,280,'TREINADORES',['PK/FK  id  ->  PerfisModelo','FK  treimanento_fk'])
note(665,'Como ler o diagrama',[
 'PK = chave primária. FK = chave estrangeira. Os números próximos às tabelas indicam a quantidade de registros em cada ponta.',
 '0..1 = vínculo opcional; 1 = exatamente um; 0..N = zero ou muitos. Linhas tracejadas representam a herança com PK compartilhada.',
 'A coleção roles_tb.perfils declara outra associação com tabela de junção implícita; consulte a observação da página 4.'
])
finish(1)
page('02  |  Planejamento e execução dos treinos','DIAGRAMA DE RELACIONAMENTOS  /  Fluxo do atleta até os exercícios')
# A single left-to-right chain keeps all links free of crossings.
chain=[(35,'ATLETAS',['PK/FK  id','Perfil do atleta']),
 (265,'SEMANA',['PK  id','FK  atleta_fk']),
 (495,'DIA',['PK  id','FK  semana_fk']),
 (725,'TREINAMENTO',['PK  id','FK  dia_fk']),
 (955,'EXERCICIO',['PK  id','FK  treinamento_fk'])]
for x,name,rows_ in chain: box(x,200,200,name,rows_)
for x,label in [(235,'atleta_fk'),(465,'semana_fk'),(695,'dia_fk'),(925,'treinamento_fk')]:
 line([(x,245),(x+30,245)])
 text(x-20,177,'1 : 0..N',11,True)
 # FK descriptions below each destination avoid crowded connection labels.
for x,label in [(265,'Pertence a um atleta'),(495,'Pertence a uma semana'),(725,'Pertence a um dia'),(955,'Pertence a um treinamento')]: text(x,302,label,10)
relation([(825,274),(825,425)],'0..1','0..N','treimanento_fk',845,350)
box(685,425,280,'TREINADORES',['PK/FK  id  ->  PerfisModelo','FK  treimanento_fk  ->  TREINAMENTO'])
relation([(135,274),(135,490),(435,490)],'0..N','0..1','atleta_trenador_fk',170,465)
box(435,425,210,'TREINADORES',['PK/FK  id','Vínculo com os atletas'])
note(625,'Regras e leitura do fluxo',[
 'Cada semana pertence a um atleta; cada dia pertence a uma semana; cada treinamento pertence a um dia; cada exercício pertence a um treinamento.',
 'As quatro FKs do fluxo são obrigatórias. As coleções podem ter zero ou vários registros; o limite de sete dias é aplicado no código Java.',
 'TREINADORES aparece duas vezes para mostrar dois vínculos distintos. A FK treimanento_fk mantém a grafia existente no projeto.'
])
finish(2)
page('03  |  Acompanhamento médico e alimentação','DIAGRAMA DE RELACIONAMENTOS  /  Vínculos médicos, recomendações e exames')
relation([(330,195),(855,195)],'0..N','0..1','atleta_medico_fk',520,170)
relation([(190,274),(190,420),(445,420)],'1','0..N','atleta_id (obrigatória)',215,395)
relation([(995,274),(995,420),(745,420)],'1','0..N','medico_id (obrigatória)',785,395)
box(50,145,280,'ATLETAS',['PK/FK  id  ->  PerfisModelo','FK  atleta_medico_fk'])
box(855,145,280,'MEDICOS',['PK/FK  id  ->  PerfisModelo','Médico responsável'])
box(445,360,300,'recomendacao_alimentar',['PK  id','FK  atleta_id  ->  ATLETAS','FK  medico_id  ->  MEDICOS','Refeição, objetivo e orientação','Datas de início e revisão'])
box(50,580,280,'EXAMA_URINA',['PK  id','Dados da amostra e do resultado','Sem FK declarada nas entidades'])
medical_note = [
 'Cada recomendação possui exatamente um atleta e um médico.',
 'Um atleta e um médico podem possuir várias recomendações.',
 'O exame de urina ainda não tem relacionamento JPA com atleta ou médico.'
]
text(395,590,'Observações do acompanhamento',14,True)
for i,t in enumerate(medical_note): text(395,625+i*25,t,11)
finish(3)
page('04  |  Cardinalidades e observações','Leitura: cada registro da tabela de origem referencia a quantidade indicada de registros da tabela de destino.')
rows=[('PerfisModelo.fal_fk','AFLIACAO.id','0..1','0..1 perfil por afiliação'),('PerfisModelo.mord','MORADIA.id','0..1','0..1 perfil por moradia'),('PerfisModelo.role_id','roles_tb.id','0..1','0..N perfis por papel'),('ATLETAS.id / MEDICOS.id / TREINADORES.id','PerfisModelo.id','1','Herança JOINED; PK também é FK'),('ATLETAS.atleta_trenador_fk','TREINADORES.id','0..1','0..N atletas por treinador'),('ATLETAS.atleta_medico_fk','MEDICOS.id','0..1','0..N atletas por médico'),('SEMANA.atleta_fk','ATLETAS.id','1','0..N semanas por atleta'),('DIA.semana_fk','SEMANA.id','1','0..N dias por semana'),('TREINAMENTO.dia_fk','DIA.id','1','0..N treinamentos por dia'),('EXERCICIO.treinamento_fk','TREINAMENTO.id','1','0..N exercícios por treinamento'),('TREINADORES.treimanento_fk','TREINAMENTO.id','0..1','0..N treinadores por treinamento'),('recomendacao_alimentar.atleta_id','ATLETAS.id','1','0..N recomendações por atleta'),('recomendacao_alimentar.medico_id','MEDICOS.id','1','0..N recomendações por médico')]
xs=[35,420,685,770]; widths=[385,265,85,386]
for x,w,t in zip(xs,widths,['Origem / FK','Destino / PK','Destino','Relação inversa']): rect(x,100,w,28,(.08,.26,.35)); text(x+7,107,t,11,True,(1,1,1))
for i,row in enumerate(rows):
 y=128+i*29
 for x,w,t in zip(xs,widths,row):
  rect(x,y,w,29,(.94,.96,.98) if i%2==0 else (1,1,1)); text(x+7,y+8,t,10)
notes=[
('Associação adicional de papéis', 'RolesModel.perfils usa @OneToMany sem mappedBy. O JPA prevê uma tabela de junção adicional, distinta de role_id.', 'Ela associa roles_tb a PerfisModelo (1:N). Seu nome e os nomes das colunas dependem das convenções do provedor.'),
('Nomes físicos e escopo', 'Os nomes de tabelas explícitos e das FKs foram mantidos como no código, inclusive AFLIACAO, EXAMA_URINA e treimanento_fk.', 'PerfisModelo não declara @Table. Nomes implícitos, caixa e snake_case dependem da estratégia de nomenclatura do Hibernate.'),
('Limites e ausência de vínculos', 'EXAMA_URINA não possui FK para atleta ou médico. O limite de sete dias é aplicado no método adicionarDia, não por uma restrição JPA.', 'Este documento descreve os mapeamentos do repositório; não foi feita consulta ao esquema de uma instância do PostgreSQL.')]
for i,(title,a,b) in enumerate(notes):
 y=535+i*83; text(35,y,title,12,True); text(35,y+23,a,11); text(35,y+41,b,11)
finish(4)
files=list((ROOT/'src/main/java/com/api/Projeto_3/model').glob('*.java'))
entries=[]
for p in files:
 s=p.read_text(); name=re.search(r'@Table\(name\s*=\s*"([^"]+)"',s)
 name=name.group(1) if name else p.stem
 fields=[]
 for typ,field in re.findall(r'private\s+([\w.<>]+)\s+(\w+)\s*(?:=[^;]*)?;',s):
  if typ.startswith('List') or typ=='static' or field=='serialVersionUID': continue
  if typ in ['PerfisModelo'] and p.stem in ['AfiliacaoModelo','MoradiaModel']: continue
  fields.append(f'{field}: {typ.replace("java.time.", "")}')
 if 'extends PerfisModelo' in s: fields.insert(0,'id: Long (PK/FK herdada)')
 entries.append((name,fields))
lookup=dict(entries)
dictionary_pages=[
 ('05  |  Dicionário: cadastro e perfis', [['PerfisModelo','roles_tb'],['AFLIACAO','MORADIA','ATLETAS','MEDICOS','TREINADORES']]),
 ('06  |  Dicionário: treinos e saúde', [['SEMANA','DIA','TREINAMENTO','EXERCICIO'],['recomendacao_alimentar','EXAMA_URINA']])]
for number,(title,groups) in enumerate(dictionary_pages,5):
 page(title,'ATRIBUTOS DAS ENTIDADES  /  Nomes e tipos Java declarados no código; atributos herdados permanecem em PerfisModelo')
 for col,names in enumerate(groups):
  x=35+col*575; y=110
  for name in names:
   fields=lookup[name]; rect(x,y,545,27,(.08,.26,.35)); text(x+12,y+6,name,12,True,(1,1,1)); y+=35
   for i,f in enumerate(fields):
    rect(x,y,545,17,(.95,.97,.99) if i%2==0 else (1,1,1))
    text(x+12,y+2,f,10); y+=17
   y+=18
 finish(number)
# Minimal PDF writer: standard Helvetica fonts and WinAnsi for Portuguese.
objects=[]
def obj(data): objects.append(data); return len(objects)
obj(b'<< /Type /Catalog /Pages 2 0 R >>'); obj(b'')
obj(b'<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>')
obj(b'<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>')
ids=[]
for stream in pages:
 sid=obj(b'<< /Length '+str(len(stream)).encode()+b' >>\nstream\n'+stream+b'\nendstream')
 ids.append(obj(f'<< /Type /Page /Parent 2 0 R /MediaBox [0 0 {W} {H}] /Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents {sid} 0 R >>'.encode()))
objects[1]=f'<< /Type /Pages /Count {len(ids)} /Kids [{" ".join(f"{i} 0 R" for i in ids)}] >>'.encode()
out=bytearray(b'%PDF-1.4\n%\xe2\xe3\xcf\xd3\n'); offsets=[0]
for i,data in enumerate(objects,1): offsets.append(len(out)); out.extend(f'{i} 0 obj\n'.encode()+data+b'\nendobj\n')
xref=len(out); out.extend(f'xref\n0 {len(objects)+1}\n0000000000 65535 f \n'.encode())
for offset in offsets[1:]: out.extend(f'{offset:010} 00000 n \n'.encode())
out.extend(f'trailer\n<< /Size {len(objects)+1} /Root 1 0 R >>\nstartxref\n{xref}\n%%EOF\n'.encode())
path=ROOT/'docs/relacionamento-banco-dados.pdf'; path.write_bytes(out); print(path)
