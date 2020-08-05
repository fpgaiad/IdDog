# IdDog

## Funcionalidades

Desenvolvido em Kotlin, contempla as seguintes funcionalidades:

* Tela inicial onde o usuário faz seu login entrando com um email válido;
* Armazenamento local do token retornado pela Api no login, para autenticar usuário nos próximos requests para a Api;
* Após login com sucesso, usuário será redirecionado para tela com lista das imagens dos cachorros retornados em GridLayout de duas colunas quando celular em modo retrato e em três colunas quando em modo paisagem.
* Usuário navega entre as quatro raças de cachorros (husky, labrador, hound e pug) via ViewPager com TabLayour;
* Ao clicar em uma imagem, imagem é exibida de forma expandida.
* Tratamento de erros.

## Estrutura de Pastas

![](/packages.png?raw=true "Estrutura de Pastas")

## Arquitetura e Libs Utilizadas

O código é estruturado com arquitetura MVVM/Clean Architecture utilizando:
* **VieModel:** Junto com LiveData o ViewModel facilita o tratamento de rotações de tela, pois as variáveis utilizadas nas views permanecem ligadas ao lifecycle do ViewModel (que não é destruído nas rotações das views);
* **LiveData:** Utilza do padrão observable para atualizar as variáveis nas views, deixando as views atualizadas com as últimas alterações nas propriedades observadas no ViewModel;
* **Material Components:** Facilitou o uso de boas práticas de design, seguindo o padrão Material Design no desenvolvimento do app;
* **Navigation Components:** Este conceito de uma Activity gerenciando vários Fragments facilita a navegação entre as telas e a troca de dados entre Fragments por meio de SafeArgs. Além disso proporciona a visualização do fluxo de telas por meio do NavGraph.
* **Dagger Hilt:** Injeção de dependência com geração de código em tempo de compilação com anotações específicas para o framework Android, facilitando sua aplicação e gerando menos linhas de código que o Dagger;
* **Coroutines:** Utilzado para tarefas assíncronas, tem fácil implementação e melhora a leitura do código;
* **Retrofit:** Biblioteca mais utilizada para requisições de dados;
* **Glide:** Biblioteca para exibição e cache de imagens com o menor tempo de download e exibição das imagens em cache, [segundo estudo](https://proandroiddev.com/coil-vs-picasso-vs-glide-get-ready-go-774add8cfd40);
* **Timber:** Utilizada para facilitar utilização de Logs durante o projeto;
* **Stetho:** Utilizada para facilitar debug das requisições para a Api.

## ScreenShots

Tela inicial com usuário deslogado

![](/login.png?raw=true "Tela inicial com usuário deslogado")

Tela inicial com usuário já logado

![](/alreadylogged.png?raw=true "Tela inicial com usuário já logado")

Tela de ViewPager com TabLayou e RecyclerView

![](/huskyPhotos.png?raw=true "Lista de fotos")

Placeholders

![](/pugAndPlaceholder.png?raw=true "Placeholders")

Tela de Detalhe

![](/pugDetail.png?raw=true "Tela de detalhe")

## Instruções para rodar o projeto

Clonar o projeto em seu computador e instalar no emulador ou device (com versão mínima: Android 4.1 - API 16).

## API Utilizada

* [Api de cachorros](https://github.com/idwall/desafios-iddog) (IdWall).
