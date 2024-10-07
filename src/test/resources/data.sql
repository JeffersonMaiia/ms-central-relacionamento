insert into atendente (id_atendente, nome, time)
values (1, 'ATENDENTE-1', 'CARTOES');
insert into atendente (id_atendente, nome, time)
values (2, 'ATENDENTE-2', 'EMPRESTIMOS');
insert into atendente (id_atendente, nome, time)
values (3, 'ATENDENTE-3', 'OUTROS');

insert into solicitacao (id_solicitacao, tipo, assunto, status, data_solicitacao ,id_atendente)
values (1, 'CARTOES', 'Problema no cartão', 'EM_ANDAMENTO', NOW(), 1);

insert into solicitacao (id_solicitacao, tipo, assunto, status, data_solicitacao ,id_atendente)
values (2, 'EMPRESTIMOS', 'Problema com emprestimo', 'AGUARDANDO_ATENDIMENTO', NOW(), null);
